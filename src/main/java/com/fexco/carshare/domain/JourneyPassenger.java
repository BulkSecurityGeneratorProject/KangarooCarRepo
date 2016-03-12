package com.fexco.carshare.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fexco.carshare.web.rest.dto.JourneyPassengerDTO;

@Entity
@Table(name = "journeypassengers")
public class JourneyPassenger {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="journeyId", nullable=false, updatable=false)
	private Journey journey;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userId", nullable=false, updatable=false)
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Journey getJourney() {
		return journey;
	}

	public void setJourney(Journey journey) {
		this.journey = journey;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public JourneyPassengerDTO toDTO() {
		JourneyPassengerDTO jpDto = new JourneyPassengerDTO();
		jpDto.setId(this.getId());
		jpDto.setJourneyId(this.getJourney().getId());
		jpDto.setUserName(this.getUser().getLogin());
		return jpDto;
	}
}
