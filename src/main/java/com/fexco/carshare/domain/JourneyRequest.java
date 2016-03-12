package com.fexco.carshare.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fexco.carshare.web.rest.dto.JourneyRequestDTO;


@Entity
@Table(name = "journeyrequests")
public class JourneyRequest {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userId", nullable=false, updatable=false)
	private User user;
	
	private String source;
	
	private String destination;
	
	private String pickUpLocation;
	
	private Date date;
	
	private String time;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public JourneyRequestDTO toDTO(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		JourneyRequestDTO dto = new JourneyRequestDTO();
		dto.setId(this.getId());
		dto.setUsername(this.getUser().getLogin());
		dto.setSource(this.getSource());
		dto.setDestination(this.getDestination());
		dto.setDate(formatter.format(this.getDate()));
		dto.setPickUpLocation(this.getPickUpLocation());
		dto.setTime(this.getTime());
		return dto;
	}
	public String getPickUpLocation() {
		return pickUpLocation;
	}
	public void setPickUpLocation(String pickUpLocation) {
		this.pickUpLocation = pickUpLocation;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

}