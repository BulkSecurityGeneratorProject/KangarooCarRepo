package com.fexco.carshare.web.rest.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class ContactPassengerDTO {
	private Long id;
	
	@NotNull
	private Long journeyRequestId;
	
	@NotNull
	private String userName;
	
	@NotNull
	@NotBlank
	private String message;
	
	@NotNull
	private Long journeyId;
	
	private Date date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getJourneyRequestId() {
		return journeyRequestId;
	}

	public void setJourneyRequestId(Long journeyRequestId) {
		this.journeyRequestId = journeyRequestId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(Long journeyId) {
		this.journeyId = journeyId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
