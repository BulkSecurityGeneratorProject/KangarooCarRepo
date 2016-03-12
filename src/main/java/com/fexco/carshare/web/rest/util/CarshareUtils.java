package com.fexco.carshare.web.rest.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fexco.carshare.domain.Journey;
import com.fexco.carshare.domain.JourneyRequest;
import com.fexco.carshare.web.rest.dto.JourneyDTO;
import com.fexco.carshare.web.rest.dto.JourneyRequestDTO;

public class CarshareUtils {
	
	
	@SuppressWarnings({ "unchecked" })
	public static JSONObject returnErrors(List<String> errors) {
		JSONObject json = new JSONObject();
    	json.put("Status","FAIL");
		json.put("Body",errors);
		return json;
	}
	

	@SuppressWarnings({ "unchecked" })
	public static JSONObject returnSuccess() {
		JSONObject json = new JSONObject();
    	json.put("Status","SUCCESS");
		json.put("Body", "OK");
		return json;
	}


	public static List<String> getListOfErrors(BindingResult result) {
		List<String> errorMessages = new LinkedList<String>();
    	if (result.hasErrors()) {
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError error : errors ) {
			     errorMessages.add(error.getDefaultMessage());
			}
		} 
    	return errorMessages;
	}


	public static List<JourneyRequestDTO> convertToJourneyRequestPage(List<JourneyRequest> content) {
		List<JourneyRequestDTO> dtos = new ArrayList<JourneyRequestDTO>();
		for(JourneyRequest journeyRequest: content){
			dtos.add(journeyRequest.toDTO());
		}
		return dtos;
	}


	public static List<JourneyDTO> convertToJourneyPage(List<Journey> content) {
		List<JourneyDTO> dtos = new ArrayList<JourneyDTO>();
		for(Journey journeyRequest: content){
			dtos.add(journeyRequest.toDTO());
		}
		return dtos;
	}
 
}
