package com.fexco.carshare.web.rest.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fexco.carshare.domain.Journey;
import com.fexco.carshare.domain.LatLng;
import com.fexco.carshare.domain.User;


//import com.fexco.model.User;

public class JourneyDTO {
	
	private Long id;
	
	@NotNull
	private String userName;
	
	@NotNull(message = "Source is compulsory")
	@NotBlank(message = "Source is compulsory")
	private String source;
	
	@NotNull(message = "Source latitude is compulsory")
	private double sourceLat;
	@NotNull(message = "Source longitude is compulsory")
	private double sourceLng;
	
	@NotNull(message = "Destination is compulsory")
	@NotBlank(message = "Destination is compulsory")
	private String destination;
	
	@NotNull(message = "Destination latitude is compulsory")
	private double destinationLat;
	@NotNull(message = "Destination longitude is compulsory")
	private double destinationLng;
	
	//@Future(message = "The date must be in the future.")
	private Date date;
	
	@Min(value=0, message = "Cost can not be less than zero.")
	@Digits(integer=4, fraction=2, message="Cost not valid.")
	private float cost;
	
	@Min(value = 1)
	@Max(value = 6)
	private int seatNumber;
	
	@NotNull(message = "Date is compulsory")
	@NotBlank(message = "Date is compulsory")
	private String time;
	
	private List<LatLng> waypts;
	
	private String luggage;
	
	private String detour;
	
	
	public String getUsername() {
		return userName;
	}
	public void setUsername(String username) {
		this.userName = username;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public double getSourceLng() {
		return sourceLng;
	}
	public void setSourceLng(double sourceLng) {
		this.sourceLng = sourceLng;
	}
	public double getSourceLat() {
		return sourceLat;
	}
	public void setSourceLat(double sourceLat) {
		this.sourceLat = sourceLat;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public double getDestinationLat() {
		return destinationLat;
	}
	public void setDestinationLat(double destinationLat) {
		this.destinationLat = destinationLat;
	}
	public double getDestinationLng() {
		return destinationLng;
	}
	public void setDestinationLng(double destinationLng) {
		this.destinationLng = destinationLng;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public float getCost() {
		return cost;
	}
	public void setCost(float cost) {
		this.cost = cost;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getSeatNumber() {
		return seatNumber;
	}
	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}
	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLuggage() {
		return luggage;
	}
	public void setLuggage(String luggage) {
		this.luggage = luggage;
	}
	public String getDetour() {
		return detour;
	}
	public void setDetour(String detour) {
		this.detour = detour;
	}
	
	
	public Journey toEntity(User user){
		@SuppressWarnings("unused")
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Journey journey = new Journey();
		journey.setUser(user);
		journey.setSource(this.getSource());
		journey.setSourceLat(this.getSourceLat());
		journey.setSourceLng(this.getSourceLng());
		journey.setDestination(this.getDestination());
		journey.setDestinationLat(this.getDestinationLat());
		journey.setDestinationLng(this.getDestinationLng());
		journey.setDate(this.getDate());
		journey.setCost(this.getCost());
		journey.setSeatNumber(this.getSeatNumber());
		journey.setTime(this.time);
		journey.setWaypts(this.getWaypts());
		journey.setLuggage(this.getLuggage());
		journey.setDetour(this.getDetour());
		return journey;
	}
	public List<LatLng> getWaypts() {
		return new ArrayList<>(new LinkedHashSet<>(waypts));
	}
	public void setWaypts(List<LatLng> waypts) {
		this.waypts = new ArrayList<>(new LinkedHashSet<>(waypts));
	}
}
