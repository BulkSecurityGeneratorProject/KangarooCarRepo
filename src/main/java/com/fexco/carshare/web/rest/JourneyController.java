package com.fexco.carshare.web.rest;

import java.util.List;

import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fexco.carshare.service.JourneyPassengerService;
import com.fexco.carshare.service.JourneyService;
import com.fexco.carshare.web.rest.dto.JourneyDTO;
import com.fexco.carshare.web.rest.dto.JourneyRequestDTO;
import com.fexco.carshare.web.rest.util.CarshareUtils;

@RestController
@RequestMapping("/api/journey")
public class JourneyController {
	
	
	@Autowired
	JourneyService journeyService;
	
	@Autowired
	JourneyPassengerService journeyPassengerService;
	
	@RequestMapping(value="/registerJourney",method=RequestMethod.POST)
	public @ResponseBody JSONObject registerJourney(@RequestBody @Valid JourneyDTO journeyDto, BindingResult result) {
		List<String> errors = journeyService.validateJourney(journeyDto,result);
		if(errors.isEmpty()){
			journeyService.createJourney(journeyDto);
			return CarshareUtils.returnSuccess();
		}
		else{
			return CarshareUtils.returnErrors(errors);
		}
	}

	@RequestMapping(value="/registerJourneyRequest",method=RequestMethod.POST)
	public @ResponseBody JSONObject registerJourneyRequest(@RequestBody @Valid JourneyRequestDTO journeyRequestDto, BindingResult result) {
		List<String> errors = journeyService.validateJourneyRequest(journeyRequestDto,result);
		if(errors.isEmpty()){
			journeyService.createJourneyRequest(journeyRequestDto);
			return CarshareUtils.returnSuccess();
		}
		else{
			return CarshareUtils.returnErrors(errors);
		}
	}
	
	
	@RequestMapping(value="/searchJourney",method=RequestMethod.POST)
	public @ResponseBody Page<JourneyDTO> searchJourney(@RequestBody JourneyDTO journeyDto, Pageable page) {
		return journeyService.getJourneySearchResults(journeyDto, page);
	}
	
	@RequestMapping(value="/searchJourneyRequest",method=RequestMethod.POST)
	public @ResponseBody Page<JourneyRequestDTO> searchJourneyRequest(
			@RequestBody JourneyRequestDTO journeyrequestDto, Pageable page) {
		return journeyService.getJourneyRequestSearchResults(journeyrequestDto, page);
	}
	
	@RequestMapping(value = "/allJourneys", method = RequestMethod.GET)
	Page<JourneyDTO> findAllJourneys(Pageable page) {
		return journeyService.getAllJourneys(page);
    }
	
	@RequestMapping(value = "/allRecentJourneys", method = RequestMethod.GET)
	Page<JourneyDTO> findAllRecentJourneys(Pageable page) {
		return journeyService.getAllRecentJourneys(page);
    }
	
	@RequestMapping(value = "/allJourneyRequests", method = RequestMethod.GET)
	Page<JourneyRequestDTO> findAllJourneyRequests(Pageable page) {
		return journeyService.getAllJourneyRequests(page);
    }
	
	@RequestMapping(value = "/allJourneys/{username}", method = RequestMethod.GET)
    Page<JourneyDTO> findAllUserJourneys(@PathVariable("username") String username, Pageable page) {
		return journeyService.getAllUserJourneys(username, page);
    }
	
	@RequestMapping(value = "/allJourneyRequests/{username}", method = RequestMethod.GET)
    Page<JourneyRequestDTO> findAllUserJourneyRequests(@PathVariable("username") String username, Pageable page) {
		return journeyService.getAllUserJourneyRequests(username, page);
    }
	
	@RequestMapping(value = "/findJourney/{id}", method = RequestMethod.GET)
    JourneyDTO findJourney(@PathVariable("id") Long id) {
		return journeyService.findJourneyById(id);
    }
	
	@RequestMapping(value = "/findJourneyRequest/{id}", method = RequestMethod.GET)
    JourneyRequestDTO findJourneyRequest(@PathVariable("id") Long id) {
		return journeyService.findJourneyRequestById(id);
    }
	
	@RequestMapping(value = "/getNumberOfBids/{id}", method = RequestMethod.GET)
    JSONObject getNumberOfBids(@PathVariable("id") Long id) {
		return journeyService.findNumberOfBids(id);
    }
	
	@RequestMapping(value = "/getCommonJourneys/{usernameMe}/{usernameOther}", method = RequestMethod.GET)
	public JSONObject getCommonJourneys(@PathVariable("usernameMe") String me, @PathVariable("usernameOther") String other){
		return journeyService.getCommonJourneys(me, other);
	}
	
	@RequestMapping(value = "/getPassengers/{journeyId}/{driverName}", method = RequestMethod.GET)
	public JSONObject getPassengers(@PathVariable("journeyId") Long journeyId, @PathVariable("driverName") String driverName){
		return journeyPassengerService.getPassengersOnJourney(journeyId, driverName);
	}
	
}
