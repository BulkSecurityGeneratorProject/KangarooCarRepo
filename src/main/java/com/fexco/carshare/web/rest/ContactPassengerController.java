package com.fexco.carshare.web.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fexco.carshare.service.ContactPassengerService;
import com.fexco.carshare.web.rest.dto.ContactPassengerDTO;
import com.fexco.carshare.web.rest.util.CarshareUtils;

@RestController
@RequestMapping("/api/contact")
public class ContactPassengerController {
	@Autowired
	ContactPassengerService contactPassengerService;
	
	@RequestMapping(value="/contactPassenger",method=RequestMethod.POST)
	public @ResponseBody JSONObject registerJourneyBid(@RequestBody @Valid ContactPassengerDTO contactPassengerDto,
			BindingResult result, HttpServletRequest request) {
		List<String> errors = contactPassengerService.validate(contactPassengerDto,result);
		if(errors.isEmpty()){
			System.out.println("no error");
			contactPassengerService .createContact(contactPassengerDto, request);
			return CarshareUtils.returnSuccess();
		}
		else{
			return CarshareUtils.returnErrors(errors);
		}
	}
	
	@RequestMapping(value = "/allContactsForJourney/{id}", method = RequestMethod.GET)
    JSONObject findAllJourneys(@PathVariable("id") Long id) {
		return contactPassengerService.getAllContacts(id);
    }
}

