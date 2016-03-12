package com.fexco.carshare.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fexco.carshare.web.rest.dto.JourneyDTO;


@Entity
@Table(name = "journeys")
public class Journey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_Id", nullable = false, updatable = false)
	private User user;

	private String source;
	private double sourceLat;
	private double sourceLng;

	private String destination;
	private double destinationLat;
	private double destinationLng;

	private Date date;

	private float cost;

	private int seatNumber;

	private String time;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "journeyId")
	private List<LatLng> waypts;

	private String luggage;

	private String detour;

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

	public double getSourceLat() {
		return sourceLat;
	}

	public void setSourceLat(double sourceLat) {
		this.sourceLat = sourceLat;
	}

	public double getSourceLng() {
		return sourceLng;
	}

	public void setSourceLng(double sourceLng) {
		this.sourceLng = sourceLng;
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

	public String getDetour() {
		return detour;
	}

	public void setDetour(String detour) {
		this.detour = detour;
	}

	public String getLuggage() {
		return luggage;
	}

	public void setLuggage(String luggage) {
		this.luggage = luggage;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public JourneyDTO toDTO() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		JourneyDTO journeyDto = new JourneyDTO();
		journeyDto.setId(this.getId());
		journeyDto.setUsername(this.getUser().getLogin());
		journeyDto.setSource(this.getSource());
		journeyDto.setSourceLat(this.getSourceLat());
		journeyDto.setSourceLng(this.getSourceLng());
		journeyDto.setDestination(this.getDestination());
		journeyDto.setDestinationLat(this.getDestinationLat());
		journeyDto.setDestinationLng(this.getDestinationLng());
		journeyDto.setDate(this.getDate());
		journeyDto.setCost(this.getCost());
		journeyDto.setSeatNumber(this.getSeatNumber());
		journeyDto.setTime(this.getTime());
		journeyDto.setWaypts(this.getWaypts());
		journeyDto.setLuggage(this.getLuggage());
		journeyDto.setDetour(this.getDetour());
		return journeyDto;
	}

	public List<LatLng> getWaypts() {
		return new ArrayList<>(new LinkedHashSet<>(waypts));
	}

	public void setWaypts(List<LatLng> waypts) {
		this.waypts = new ArrayList<>(new LinkedHashSet<>(waypts));
	}

}
