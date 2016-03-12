package com.fexco.carshare.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fexco.carshare.domain.Journey;
import com.fexco.carshare.domain.JourneyPassenger;
import com.fexco.carshare.domain.JourneyRequest;
import com.fexco.carshare.domain.LatLng;
import com.fexco.carshare.domain.Review;
import com.fexco.carshare.domain.User;
import com.fexco.carshare.repository.JourneyBidRepository;
import com.fexco.carshare.repository.JourneyRepository;
import com.fexco.carshare.repository.JourneyRequestRepository;
import com.fexco.carshare.repository.LatLngRepository;
import com.fexco.carshare.repository.ReviewRepository;
import com.fexco.carshare.repository.UserRepository;
import com.fexco.carshare.web.rest.dto.JourneyDTO;
import com.fexco.carshare.web.rest.dto.JourneyRequestDTO;
import com.fexco.carshare.web.rest.util.CarshareUtils;

@Service
@Transactional
public class JourneyService {
	
	@Inject
	UserRepository userRepository;

	@Inject
	JourneyRepository journeyRepository;

	@Inject
	JourneyRequestRepository journeyRequestRepository;
	
	@Inject
	JourneyBidRepository journeyBidRepository;

	@Inject
	LatLngRepository latLngRepository;
	
	@Inject
	JourneyPassengerService journeyPassengerService;
	
	@Inject
	ReviewRepository reviewRepository;


	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	public void createJourney(JourneyDTO journeyDto) {
		Journey journey = journeyDto.toEntity(userRepository.findByLogin(journeyDto.getUsername()));
		journey.setWaypts(removeDuplicates(journey.getWaypts()));
		latLngRepository.save(journey.getWaypts());
		journeyRepository.save(journey);
		journeyPassengerService.addJourneyPassenger(journey, journey.getUser());
	}
 
	private List<LatLng> removeDuplicates(List<LatLng> waypts) {
		int i, j;
		List<LatLng> newList = waypts;
		Set<Integer> removeSet = new TreeSet<Integer>();
		List<Integer> removeList = new ArrayList<Integer>();
		for(i=0; i<waypts.size(); i++){
			Double lat = waypts.get(i).getLat();
			Double lng= waypts.get(i).getLng();
			for(j=(i+1); j<=waypts.size()-1;j++){
				Double lat2 = waypts.get(j).getLat();
				Double lng2= waypts.get(j).getLng();
				if((lat - lat2 == 0)&&(lng - lng2 == 0)){
					removeSet.add(j);
				}
			}
		}
		removeList.addAll(removeSet);
		for(i=removeList.size()-1; i>=0;i--){
			int removeIndex = removeList.get(i);
			newList.remove(removeIndex);
		}
		return newList;
	}

	public void createJourneyRequest(JourneyRequestDTO journeyRequestDto) {
		JourneyRequest journeyRequest = journeyRequestDto.toEntity(userRepository.findByLogin(journeyRequestDto.getUsername()));
		journeyRequestRepository.save(journeyRequest);
	}
	
	public Page<JourneyDTO> getAllJourneys(Pageable page) {
		Page<Journey> journeys = journeyRepository.findAll(page);
		Page<JourneyDTO> journeyDtos = new PageImpl<JourneyDTO>(convertToJourneyPage
				(journeys.getContent()),page, journeys.getTotalElements());
		return journeyDtos;
	}
	
	
	
	public Page<JourneyDTO> getAllRecentJourneys(Pageable page) {
		Page<Journey> journeys = journeyRepository.findAllRecent(page, getTwoDaysAgo());
		Page<JourneyDTO> journeyDtos = new PageImpl<JourneyDTO>(convertToJourneyPage
				(journeys.getContent()),page, journeys.getTotalElements());
		return journeyDtos;
	}
	
	public Page<JourneyRequestDTO> getAllJourneyRequests(Pageable page) {
		Page<JourneyRequest> journeyRequests = journeyRequestRepository.findAll(page);
		Page<JourneyRequestDTO> journeyRequestDtos = new PageImpl<JourneyRequestDTO>(CarshareUtils.convertToJourneyRequestPage
				(journeyRequests.getContent()),page, journeyRequests.getTotalElements());
		return journeyRequestDtos;
	}

	public Page<JourneyDTO> getAllUserJourneys(String username, Pageable page) {
		Page<Journey> journeys = journeyRepository.findAllByUserId(
				userRepository.findByLogin(username).getId(), page);
		Page<JourneyDTO> journeyDtos = new PageImpl<JourneyDTO>(CarshareUtils.convertToJourneyPage
				(journeys.getContent()),page, journeys.getTotalElements());
		return journeyDtos;
	}
	
	public Page<JourneyRequestDTO> getAllUserJourneyRequests(String username, Pageable page) {
		
		Page<JourneyRequest> journeyRequests = journeyRequestRepository
				.findAllByUserId(userRepository.findByLogin(username).getId(), page);
		Page<JourneyRequestDTO> journeyRequestDtos = new PageImpl<JourneyRequestDTO>(CarshareUtils.convertToJourneyRequestPage
				(journeyRequests.getContent()),page, journeyRequests.getTotalElements());
		return journeyRequestDtos;
	}
	
	public List<JourneyDTO> convertToJourneyPage(List<Journey> content) {
		List<JourneyDTO> dtos = new ArrayList<JourneyDTO>();
		for(Journey journeyRequest: content){
			dtos.add(journeyRequest.toDTO());
		}
		return dtos;
	}

	
	public Page<JourneyDTO> getJourneySearchResults(JourneyDTO journeyDto, Pageable page) {
		Page<Journey> journeys = getJournysQuery(journeyDto, page);
		Page<JourneyDTO> journeyDtos = new PageImpl<JourneyDTO>(convertToJourneyPage
				(journeys.getContent()),page, journeys.getTotalElements());
		return journeyDtos;
	}

	public Page<JourneyRequestDTO> getJourneyRequestSearchResults(
			JourneyRequestDTO journeyrequestDto, Pageable page) {
		Page<JourneyRequest> journeyRequests = 	getJournyRequestsQuery(journeyrequestDto, page);
		Page<JourneyRequestDTO> journeyRequestDtos = new PageImpl<JourneyRequestDTO>(CarshareUtils.convertToJourneyRequestPage
				(journeyRequests.getContent()),page, journeyRequests.getTotalElements());
		return journeyRequestDtos;
	}

	public List<String> validateJourney(JourneyDTO journeyDto,
			BindingResult result) {
		
		
		
		Date date = getYesterday();
		if (journeyDto.getDate().before(date)) {
			result.rejectValue("date", "error.user",
					"Date must be in the future!!");
		} else if (journeyDto.getDate().after(nextYear())) {
			result.rejectValue("date", "error.user",
					"Date must be within the next year!!");
		}
		

		List<String> errorMessages = new LinkedList<String>();
		if (result.hasErrors()) {

			List<FieldError> errors = result.getFieldErrors();
			for (FieldError error : errors) {
				errorMessages.add(error.getDefaultMessage());
			}
		}
		return errorMessages;
	}

	private Date getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date date = cal.getTime();
		return date;
	}
	private Date getTwoDaysAgo() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		Date date = cal.getTime();
		return date;
	}
	
	public List<String> validateJourneyRequest(
			JourneyRequestDTO journeyRequestDto, BindingResult result) {
		Date journeyDate = null;
		try {
			journeyDate = formatter.parse(journeyRequestDto.getDate());
			if (journeyDate.before(new Date())) {
				result.rejectValue("date", "error.user",
						"Date must be in the future!!");
			} else if (journeyDate.after(nextYear())) {
				result.rejectValue("date", "error.user",
						"Date must be within the next year!!");
			}
		} catch (ParseException e) {
			result.rejectValue("date", "error.user",
					"An error occured with the date you picked!!!");
		}

		List<String> errorMessages = new LinkedList<String>();
		if (result.hasErrors()) {

			List<FieldError> errors = result.getFieldErrors();
			for (FieldError error : errors) {
				errorMessages.add(error.getDefaultMessage());
			}
		}
		return errorMessages;
	}

	private Page<Journey> getJournysQuery(JourneyDTO journeyDto, Pageable page) {
		Date date = null;
		
			date = journeyDto.getDate();
		

		Page<Journey> journeys = null;
		if (date == null) {
			journeys = journeyRepository
					.findAllByUserandSourceAndDestinationQuery(
							journeyDto.getUsername(), journeyDto.getSource(),
							journeyDto.getDestination(),page, getTwoDaysAgo());
		} else {
			journeys = journeyRepository
					.findAllByUserandSourceAndDestinationAndDateQuery(
							journeyDto.getUsername(), journeyDto.getSource(),
							journeyDto.getDestination(), date, page, getTwoDaysAgo());
		}
		return journeys;
	}

	private Page<JourneyRequest> getJournyRequestsQuery(
			JourneyRequestDTO journeyrequestDto, Pageable page) {
		Date date = null;
		try {
			date = formatter.parse(journeyrequestDto.getDate());
		} catch (ParseException e) {
		}

		Page<JourneyRequest> journeyRequests = null;
		if (date == null) {
			journeyRequests = journeyRequestRepository
					.findAllByUserandSourceAndDestinationQuery(
							journeyrequestDto.getUsername(),
							journeyrequestDto.getSource(),
							journeyrequestDto.getDestination(), page);
		} else {
			journeyRequests = journeyRequestRepository
					.findAllByUserandSourceAndDestinationAndDateQuery(
							journeyrequestDto.getUsername(),
							journeyrequestDto.getSource(),
							journeyrequestDto.getDestination(), date, page);
		}
		return journeyRequests;
	}

	private Date nextYear() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 1);
		Date oneYearsTime = cal.getTime();
		return oneYearsTime;
	}

	public JourneyDTO findJourneyById(Long id) {
		return journeyRepository.findOne(id).toDTO();
	}
	
	public JourneyRequestDTO findJourneyRequestById(Long id) {
		return journeyRequestRepository.findOne(id).toDTO();
	}

	@SuppressWarnings("unchecked")
	public JSONObject findNumberOfBids(Long id) {
		JSONObject json = new JSONObject();
		Long number = journeyBidRepository.countByJourneyIdAndStatus(id, "pending");
		json.put("number", number);
		return json;
	}

	@SuppressWarnings("unchecked")
	public JSONObject getCommonJourneys(String me, String other){
		User userMe = userRepository.findByLogin(me);
		User userOther = userRepository.findByLogin(other);
		ArrayList<JourneyPassenger> journeyPassengers = journeyPassengerService.getCommon(userMe, userOther);
		ArrayList<JourneyDTO> commonJourneys = new ArrayList<JourneyDTO>();
		for(JourneyPassenger jp: journeyPassengers){
			if(noReviewForJourney(jp, userOther))
				commonJourneys.add(jp.getJourney().toDTO());
		}
		JSONObject json = new JSONObject();
		json.put("common", commonJourneys);
		return json;
	}

	private boolean noReviewForJourney(JourneyPassenger jp, User userOther) {
		Review review = reviewRepository.findByReceiverUserAndSenderUserAndJourney(userOther, jp.getUser(), jp.getJourney());
		if(jp.getJourney().getDate().after(new Date())){
			return false;
		}
		return review == null ? true:false;
	}
}

