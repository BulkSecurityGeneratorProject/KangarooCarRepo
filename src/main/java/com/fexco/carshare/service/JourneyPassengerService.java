package com.fexco.carshare.service;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.fexco.carshare.domain.Journey;
import com.fexco.carshare.domain.JourneyPassenger;
import com.fexco.carshare.domain.User;
import com.fexco.carshare.repository.JourneyPassengerRepository;
import com.fexco.carshare.web.rest.dto.JourneyPassengerDTO;

@Service
@Transactional
public class JourneyPassengerService {
	
	@Inject
	JourneyPassengerRepository journeyPassengerRepository;
	
	public void addJourneyPassenger(Journey j, User u){
		JourneyPassenger jp = new JourneyPassenger();
		jp.setJourney(j);
		jp.setUser(u);
		journeyPassengerRepository.save(jp);
	}
	
	public ArrayList<JourneyPassenger> getCommon(User me, User other){
		ArrayList<JourneyPassenger> list;
		if(!me.equals(other)){
			list = journeyPassengerRepository.findCommon(me, other, new Sort(new Order(Direction.DESC,"id")));
		}
		else{
			list = new ArrayList<JourneyPassenger>();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getPassengersOnJourney(Long journeyId, String driverName){
		JSONObject json = new JSONObject();
		ArrayList<JourneyPassenger> passengers = journeyPassengerRepository.getPassengers(journeyId, driverName);
		ArrayList<JourneyPassengerDTO> passengersDto = new ArrayList<JourneyPassengerDTO>();
		for(JourneyPassenger jp : passengers){
			passengersDto.add(jp.toDTO());
		}
		json.put("passengers", passengersDto);
		return json;
	}

}
