package com.fexco.carshare.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fexco.carshare.domain.Journey;
import com.fexco.carshare.domain.JourneyBid;
import com.fexco.carshare.domain.Notification;
import com.fexco.carshare.domain.User;
import com.fexco.carshare.repository.JourneyBidRepository;
import com.fexco.carshare.repository.JourneyRepository;
import com.fexco.carshare.repository.UserRepository;
import com.fexco.carshare.web.rest.NotifyController;
import com.fexco.carshare.web.rest.dto.JourneyBidDTO;
import com.fexco.carshare.web.rest.util.Status;

@Service
@Transactional
public class JourneyBidService {
	@Autowired
	JourneyBidRepository journeyBidRepository;
	
	@Autowired
	JourneyRepository journeyRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Inject
    private MailService mailService;
	
	@Inject
	JourneyPassengerService journeyPassengerService;
	
	@Inject
	NotifyController notifyController;
	

	public void createJourneyBid(JourneyBidDTO journeyBidDto, HttpServletRequest request) {
		JourneyBid journeyBid = new JourneyBid();
		dtoToEntity(journeyBid, journeyBidDto);
		JourneyBid jBid = journeyBidRepository.save(journeyBid);
		User senderUser = jBid.getUser();
		User receiverUser = jBid.getJourney().getUser();
		Long journeyId = jBid.getJourney().getId();
		String[] link = {"manageBids", "journeyId", journeyId.toString()};
		Notification notification = new Notification(senderUser.getLogin(), receiverUser.getLogin(),
				" has bid on one of your journeys", link, "bid", jBid.getId());
		notifyController.sendNotification(receiverUser.getLogin(), notification);
		String requestURL = request.getRequestURL().toString();
		String servletPath = request.getServletPath();
		String appURL = requestURL.substring(0, requestURL.indexOf(servletPath));
		mailService.sendJourneyBidReceivedEmail(receiverUser, appURL, senderUser, jBid, jBid.getJourney());
	}
	
	public void updateBidStatus(Long id, String status, HttpServletRequest request) {
		JourneyBid userBid = journeyBidRepository.findOne(id);
		if(userBid.getStatus().equals("pending")){
			userBid.setStatus(status);
			JourneyBid jbid = journeyBidRepository.save(userBid);
			if(status.equals("accepted")){
				User user = userRepository.findByLogin(jbid.getUser().getLogin());
				Journey journey = journeyRepository.findOne(jbid.getJourney().getId());
				journey.setSeatNumber(journey.getSeatNumber()-1);
				journeyRepository.save(journey);
				journeyPassengerService.addJourneyPassenger(journey, user);
			}
		    User senderUser = jbid.getJourney().getUser();
		    Long journeyId = jbid.getJourney().getId();
			User receiverUser = userBid.getUser();
			String[] link = {"journey", "journeyId", journeyId.toString()};
			Notification notification = new Notification(senderUser.getLogin(), receiverUser.getLogin(), 
					" has "+ status+" the bid you made on their journey", link, "bidStatus", jbid.getId());
			notifyController.sendNotification(receiverUser.getLogin(), notification);
			String requestURL = request.getRequestURL().toString();
			String servletPath = request.getServletPath();
			String appURL = requestURL.substring(0, requestURL.indexOf(servletPath));
			mailService.bidStatusChange(receiverUser, appURL, senderUser, status);
		}
	}
	
	public JSONObject getAllJourneyBids(Long id) {
		return findAllJourneyBidsInDatabase(id);
	}
	
	public Page<JourneyBidDTO> getAllBids(String username, Pageable page) {
		User user = userRepository.findByLogin(username);
		Page<JourneyBid> journeyBids = journeyBidRepository.findAllByUser(user, page);
		Page<JourneyBidDTO> journeyBidDTOs = new PageImpl<JourneyBidDTO>(convertToJourneyBidPage
				(journeyBids.getContent()),page, journeyBids.getTotalElements());
		return journeyBidDTOs;
	}
	
	private List<JourneyBidDTO> convertToJourneyBidPage(
			List<JourneyBid> content) {
		List<JourneyBidDTO> dtos = new ArrayList<JourneyBidDTO>();
		for(JourneyBid journeyBid: content){
			JourneyBidDTO jbd = new JourneyBidDTO();
			entityToDto(journeyBid, jbd);
			dtos.add(jbd);
		}
		return dtos;
	}

	private void dtoToEntity(JourneyBid journeyBid, JourneyBidDTO journeyBidDto) {
		journeyBid.setJourney(journeyRepository.findOne(journeyBidDto.getJourneyId()));
		journeyBid.setUser(userRepository.findByLogin(journeyBidDto.getUserName()));
		journeyBid.setBid(journeyBidDto.getBid());
		journeyBid.setPickupLocation(journeyBidDto.getPickupLocation());
		journeyBid.setDropOffLocation(journeyBidDto.getDropOffLocation());
	}

	public List<String> validateJourney(String userName, JourneyBidDTO journeyBidDto,BindingResult result) {
		Iterable<JourneyBid> jb = journeyBidRepository.findAllByUserAndJourney(userRepository.findByLogin(userName),
				journeyRepository.findOne(journeyBidDto.getJourneyId()));
		for(JourneyBid j:jb) {
			if(!j.getStatus().equalsIgnoreCase(Status.REJECTED)){
				result.rejectValue("userName", "error.user", "Cannot make this bid, A current bid is pending");
				break;
	    	}
		}
		
		List<String> errorMessages = new LinkedList<String>();
    	if (result.hasErrors()) {
			
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError error : errors ) {
			     errorMessages.add(error.getDefaultMessage());
			}
		} 
    	return errorMessages;
	}

	@SuppressWarnings("unchecked")
	private JSONObject findAllJourneyBidsInDatabase(Long id) {
		JSONObject journeyAsJson = new JSONObject();
		Iterable<JourneyBid> journeyBids = journeyBidRepository.findAllByJourney(journeyRepository.findOne(id),
				new Sort(Sort.Direction.DESC, "id"));
		List<JourneyBidDTO> journeyBidDtos = new LinkedList<JourneyBidDTO>();
		for(JourneyBid j: journeyBids){
			JourneyBidDTO journeyBidDto = new JourneyBidDTO();
			entityToDto(j, journeyBidDto);
			journeyBidDtos.add(journeyBidDto);
		}
		journeyAsJson.put("enquires", journeyBidDtos);
        return journeyAsJson;
	}
	

	private void entityToDto(JourneyBid j, JourneyBidDTO journeyBidDto) {
		journeyBidDto.setId(j.getId());
		journeyBidDto.setJourneyId(j.getJourney().getId());
		journeyBidDto.setUserName(j.getUser().getLogin());
		journeyBidDto.setBid(j.getBid());
		journeyBidDto.setStatus(j.getStatus());
		journeyBidDto.setPickupLocation(j.getPickupLocation());
		journeyBidDto.setDropOffLocation(j.getDropOffLocation());
	}

	

	@SuppressWarnings("unchecked")
	public JSONObject getNumberBidsByStatus(Long journeyId, String status){
		Long number = journeyBidRepository.countByJourneyIdAndStatus(journeyId, status);
		JSONObject json = new JSONObject();
		json.put("number", number);
		return json;
	}
	
	
	
}
