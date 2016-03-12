package com.fexco.carshare.web.rest.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fexco.carshare.domain.JourneyRequest;
import com.fexco.carshare.domain.User;


public class JourneyRequestDTO {
	private Long id;
	
	@NotNull
	private String username;
	
	@NotNull(message = "Source is compulsory")
	@NotBlank(message = "Source is compulsory")
	private String source;
	
	@NotNull(message = "Destination is compulsory")
	@NotBlank(message = "Destination is compulsory")
	private String destination;
	
	@NotNull(message = "Add a pickup location")
	@NotBlank(message = "Add a pickup location")
	private String pickUpLocation;
	
	@NotNull(message = "Date is compulsory")
	@NotBlank(message = "Date is compulsory")
	private String date;
	
	@NotNull(message = "Add a pickup time")
	@NotBlank(message = "Add a pickup time")
	private String time;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public JourneyRequest toEntity(User user){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		JourneyRequest journeyRequest = new JourneyRequest();
		journeyRequest.setUser(user);
		journeyRequest.setSource(this.getSource());
		journeyRequest.setDestination(this.getDestination());
		try {
			journeyRequest
					.setDate(formatter.parse(this.getDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		journeyRequest.setPickUpLocation(this.getPickUpLocation());
		journeyRequest.setTime(this.getTime());
		return journeyRequest;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPickUpLocation() {
		return pickUpLocation;
	}
	public void setPickUpLocation(String pickUpLocation) {
		this.pickUpLocation = pickUpLocation;
	}
}
