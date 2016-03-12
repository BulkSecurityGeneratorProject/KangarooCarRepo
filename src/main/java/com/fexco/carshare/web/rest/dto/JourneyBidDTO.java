package com.fexco.carshare.web.rest.dto;

import javax.validation.constraints.NotNull;

import com.fexco.carshare.web.rest.util.Status;



public class JourneyBidDTO {
	private Long id;
	
	@NotNull
	private Long journeyId;
	
	@NotNull
	private String userName;
	
	@NotNull
	private float bid;
	
	@NotNull
	private String pickupLocation;
	
	@NotNull
	private String dropOffLocation;
	
	private String status = Status.PENDING;

	public Long getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(Long journeyId) {
		this.journeyId = journeyId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public float getBid() {
		return bid;
	}

	public void setBid(float bid) {
		this.bid = bid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public String getDropOffLocation() {
		return dropOffLocation;
	}

	public void setDropOffLocation(String dropOffLocation) {
		this.dropOffLocation = dropOffLocation;
	}

}