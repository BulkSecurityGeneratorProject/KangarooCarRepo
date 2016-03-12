package com.fexco.carshare.web.rest.dto;

import javax.validation.constraints.NotNull;

public class ProfileDTO {
	
	private Long id;
	
	@NotNull
	private String userName;
	
	private String bio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}
}
