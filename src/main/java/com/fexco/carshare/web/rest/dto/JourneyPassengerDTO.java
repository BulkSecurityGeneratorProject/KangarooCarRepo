package com.fexco.carshare.web.rest.dto;

import javax.validation.constraints.NotNull;

public class JourneyPassengerDTO {
	
	private Long id;
	
	@NotNull
	private Long journeyId;
	
	@NotNull
	private String userName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
}
