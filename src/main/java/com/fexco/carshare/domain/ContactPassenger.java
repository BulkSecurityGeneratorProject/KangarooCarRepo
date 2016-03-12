package com.fexco.carshare.domain;

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


@Entity
@Table(name = "contactPassengers")
public class ContactPassenger {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="journeyRequestId", nullable=false, updatable=false)
	private JourneyRequest journeyRequest;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="userId", nullable=false, updatable=false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="journeyId", nullable=false, updatable=false)
	private Journey journey;
	
	@Column(length=400)
	private String message;
	
	private Date date = new Date();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public JourneyRequest getJourneyRequest() {
		return journeyRequest;
	}

	public void setJourneyRequest(JourneyRequest journeyRequest) {
		this.journeyRequest = journeyRequest;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
