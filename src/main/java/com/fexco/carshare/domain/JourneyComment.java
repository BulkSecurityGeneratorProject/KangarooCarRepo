package com.fexco.carshare.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "journeycomments")
public class JourneyComment implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="sender_user_Id", nullable=false, updatable=false)
	private User senderUser;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="journey_Id", nullable=false, updatable=false)
	private Journey journey;
	
	@Column(length=512)
	private String message;
	
	private boolean reply;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="commentId", nullable=true, updatable=false)
	private JourneyComment comment;
	
	private Date date = new Date();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getSenderUser() {
		return senderUser;
	}

	public void setSenderUser(User senderUser) {
		this.senderUser = senderUser;
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

	public JourneyComment getComment() {
		return comment;
	}

	public void setComment(JourneyComment journeyComment) {
		this.comment = journeyComment;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Journey getJourney() {
		return journey;
	}

	public void setJourney(Journey journey) {
		this.journey = journey;
	}
}
