package com.fexco.carshare.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fexco.carshare.service.MyProfileService;
import com.fexco.carshare.web.rest.dto.ProfileDTO;

@RestController
@RequestMapping("/api/myProfile")
public class MyProfileController {
	
	@Autowired
	MyProfileService myProfileService;
	
	@RequestMapping(value = "/findProfile/{username}", method = RequestMethod.GET)
	ProfileDTO findProfile(@PathVariable("username") String username) {
		return myProfileService.getProfileByUserId(username);
    }
	
	@RequestMapping(value="/addProfile",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> addProfile(
			@RequestBody ProfileDTO profileDto) {
		return myProfileService.addProfile(profileDto);
	}
}
