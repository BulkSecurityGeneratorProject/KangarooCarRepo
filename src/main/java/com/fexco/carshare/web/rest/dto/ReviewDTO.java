package com.fexco.carshare.web.rest.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class ReviewDTO {
	
	private Long id;
	
	@NotNull
	private Long journeyId;
	
	@NotNull
	private String senderUserName;
	
	@NotNull
	private String receiverUserName;
	
	@NotNull
	private String message;
	
	@NotNull
	private int value;
	
	private Date date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSenderUserName() {
		return senderUserName;
	}

	public void setSenderUserName(String senderUserName) {
		this.senderUserName = senderUserName;
	}

	public String getReceiverUserName() {
		return receiverUserName;
	}

	public void setReceiverUserName(String receiverUserName) {
		this.receiverUserName = receiverUserName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(Long journeyId) {
		this.journeyId = journeyId;
	}
}
