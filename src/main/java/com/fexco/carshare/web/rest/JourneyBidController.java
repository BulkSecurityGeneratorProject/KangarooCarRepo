package com.fexco.carshare.web.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import com.fexco.carshare.service.JourneyBidService;
import com.fexco.carshare.web.rest.dto.JourneyBidDTO;
import com.fexco.carshare.web.rest.util.CarshareUtils;

@RestController
@RequestMapping("/api/journeyBid")
public class JourneyBidController {
	@Autowired
	JourneyBidService journeyBidService;

	@RequestMapping(value="/registerJourneyBid/{userName}",method=RequestMethod.POST)
	public @ResponseBody JSONObject registerJourneyBid(@PathVariable("userName") String userName,
			@RequestBody @Valid JourneyBidDTO journeyBidDto, BindingResult result, HttpServletRequest request) {
	List<String> errors = journeyBidService.validateJourney(userName,journeyBidDto,result);
		if(errors.isEmpty()){
			journeyBidService .createJourneyBid(journeyBidDto, request);
			return CarshareUtils.returnSuccess();
		}
		else{
			return CarshareUtils.returnErrors(errors);
		}
	}
	
	@RequestMapping(value = "/allBidsForJourney/{id}", method = RequestMethod.GET)
    JSONObject findAllJourneys(@PathVariable("id") Long id) {
		return journeyBidService.getAllJourneyBids(id);
    }
	
	@RequestMapping(value = "/bidForJourney/{id}/{status}", method = RequestMethod.GET)
    void findJourneys(@PathVariable("id") Long id, @PathVariable("status") String status, HttpServletRequest request) {
		journeyBidService.updateBidStatus(id, status, request);
    }
	
	@RequestMapping(value = "/allBids/{username}", method = RequestMethod.GET)
    Page<JourneyBidDTO> findAllBids(@PathVariable("username") String username, Pageable page) {
		return journeyBidService.getAllBids(username, page);
    }
	
	@RequestMapping(value = "/countBids/{id}/{status}", method = RequestMethod.GET)
    JSONObject countBids(@PathVariable("id") Long id, @PathVariable("status") String status, HttpServletRequest request) {
		return journeyBidService.getNumberBidsByStatus(id, status);
    }
}
