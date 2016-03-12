package com.fexco.carshare.service;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fexco.carshare.domain.ContactPassenger;
import com.fexco.carshare.domain.Notification;
import com.fexco.carshare.domain.User;
import com.fexco.carshare.repository.ContactPassengerRepository;
import com.fexco.carshare.repository.JourneyRepository;
import com.fexco.carshare.repository.JourneyRequestRepository;
import com.fexco.carshare.repository.UserRepository;
import com.fexco.carshare.web.rest.NotifyController;
import com.fexco.carshare.web.rest.dto.ContactPassengerDTO;

@Service
@Transactional
public class ContactPassengerService {
	@Autowired
	ContactPassengerRepository contactPassengerRepository;
	
	@Autowired
	JourneyRequestRepository journeyRequestRepository;
	
	@Autowired
	JourneyRepository journeyRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Inject
	NotifyController notifyController;
	
	@Autowired
	MailService mailService;

	public void createContact(ContactPassengerDTO contactPassengerDto, HttpServletRequest request) {
		ContactPassenger contactPassenger = new ContactPassenger();
		dtoToEntity(contactPassenger,contactPassengerDto);
		ContactPassenger contact = contactPassengerRepository.save(contactPassenger);
		User senderUser = contact.getUser();
		User receiverUser = contact.getJourneyRequest().getUser();
		Long journeyRequestId = contact.getJourneyRequest().getId();
		String[] link = {"viewContact", "journeyRequestId", journeyRequestId.toString()};
		Notification notification = new Notification(senderUser.getLogin(), receiverUser.getLogin(), 
				" responded to your journey request", link, "offer", contact.getId());
		notifyController.sendNotification(receiverUser.getLogin(), notification);
		String requestURL = request.getRequestURL().toString();
		String servletPath = request.getServletPath();
		String appURL = requestURL.substring(0, requestURL.indexOf(servletPath));
		mailService.sendOfferReceivedEmail(receiverUser, appURL, senderUser);
	}
	
	public JSONObject getAllContacts(Long id) {
		return findAllContactPassengersInDatabase(id);
	}
	
	public List<String> validate(ContactPassengerDTO contactPassengerDto,
			BindingResult result) {
		List<String> errorMessages = new LinkedList<String>();
    	if (result.hasErrors()) {
			
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError error : errors ) {
			     errorMessages.add(error.getDefaultMessage());
			}
		} 
    	return errorMessages;
	}

	private void dtoToEntity(ContactPassenger contactPassenger,
			ContactPassengerDTO contactPassengerDto) {
		contactPassenger.setJourneyRequest(journeyRequestRepository.findOne(contactPassengerDto.getJourneyRequestId()));
		contactPassenger.setUser(userRepository.findByLogin(contactPassengerDto.getUserName()));
		contactPassenger.setMessage(contactPassengerDto.getMessage());
		contactPassenger.setJourney(journeyRepository.findOne(contactPassengerDto.getJourneyId()));
	}

	@SuppressWarnings("unchecked")
	private JSONObject findAllContactPassengersInDatabase(Long id) {
		JSONObject contactPassengersAsJson = new JSONObject();
		Iterable<ContactPassenger> contactPassengers = contactPassengerRepository.findAllByJourneyRequest(journeyRequestRepository.findOne(id),
				new Sort(Sort.Direction.DESC, "id"));
		List<ContactPassengerDTO> contactPassengerDtos = new LinkedList<ContactPassengerDTO>();
		for(ContactPassenger c: contactPassengers){
			ContactPassengerDTO contactPassengerDto = new ContactPassengerDTO();
			entityToDto(c, contactPassengerDto);
			contactPassengerDtos.add(contactPassengerDto);
		}
		contactPassengersAsJson.put("enquires", contactPassengerDtos);
        return contactPassengersAsJson;
	}

	private void entityToDto(ContactPassenger c,
			ContactPassengerDTO contactPassengerDto) {
		contactPassengerDto.setId(c.getId());
		contactPassengerDto.setJourneyRequestId(c.getJourneyRequest().getId());
		contactPassengerDto.setUserName(c.getUser().getLogin());
		contactPassengerDto.setMessage(c.getMessage());
		contactPassengerDto.setJourneyId(c.getJourney().getId());
		contactPassengerDto.setDate(c.getDate());
	}

}
