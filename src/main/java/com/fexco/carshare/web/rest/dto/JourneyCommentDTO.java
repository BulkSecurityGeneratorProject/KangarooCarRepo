package com.fexco.carshare.web.rest.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class JourneyCommentDTO {
	
private Long id;
	
	@NotNull
	private String senderUserName;
	
	@NotNull
	private Long journeyId;
	
	@NotNull
	private String message;
	
	@NotNull
	private boolean reply;
	
	private Long commentId;
	
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


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isReply() {
		return reply;
	}

	public void setReply(boolean reply) {
		this.reply = reply;
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
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
