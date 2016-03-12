package com.fexco.carshare.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fexco.carshare.domain.Profile;
import com.fexco.carshare.domain.User;
import com.fexco.carshare.repository.ProfileRepository;
import com.fexco.carshare.repository.UserRepository;
import com.fexco.carshare.web.rest.dto.ProfileDTO;

@Service
@Transactional
public class MyProfileService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProfileRepository profileRepository;
	
	public ProfileDTO getProfileByUserId(String username){
		User user = userRepository.findByLogin(username);
		Profile profile = profileRepository.findOneByUser(user);
		if(profile != null){
			return toDTO(profile);
		}
		else{
			return new ProfileDTO();
		}
	}
	
	public ResponseEntity<?> addProfile(ProfileDTO profileDto){
		User user = userRepository.findByLogin(profileDto.getUserName());
		Profile profile = profileRepository.findOneByUser(user);
		if(profile == null){
			profile = new Profile();
			profile = toEntity(profileDto);
		}
		else{
			profile.setBio(profileDto.getBio());
		}
		profileRepository.save(profile);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	public Profile toEntity(ProfileDTO profileDto){
		Profile profile = new Profile();
		profile.setUser(userRepository.findByLogin(profileDto.getUserName()));
		profile.setBio(profileDto.getBio());
		return profile;
	}
	
	public ProfileDTO toDTO(Profile profile){
		ProfileDTO profileDto = new ProfileDTO();
		profileDto.setId(profile.getId());
		profileDto.setUserName(profile.getUser().getLogin());
		profileDto.setBio(profile.getBio());
		return profileDto;
	}
}
