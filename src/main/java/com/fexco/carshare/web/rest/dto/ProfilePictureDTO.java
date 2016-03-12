package com.fexco.carshare.web.rest.dto;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

public class ProfilePictureDTO {
	
	@NotNull
	private String userName;
	
	@NotNull
	private MultipartFile profilePicture;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public MultipartFile getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(MultipartFile profilePicture) {
		this.profilePicture = profilePicture;
	}
	
	
}
