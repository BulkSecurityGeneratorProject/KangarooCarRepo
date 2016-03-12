package com.fexco.carshare.web.rest;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fexco.carshare.service.ProfileService;
import com.fexco.carshare.web.rest.dto.ProfilePictureDTO;

@Controller
@RequestMapping("/user/profile")
public class ProfileController {
	@Autowired
	ProfileService profileService;
	
	@RequestMapping(value="/addPicture", method=RequestMethod.POST)
	public @ResponseBody void addProfilePicture(@RequestParam("userName") String uName, @RequestParam("profilePicture")MultipartFile file){
		ProfilePictureDTO pictureDTO = new ProfilePictureDTO();
		pictureDTO.setUserName(uName);
		pictureDTO.setProfilePicture(file);
		profileService.addProfilePicture(pictureDTO);
	}
	
	
	public static final String BASE_PATH = "/srv/carshare/resources";

    @RequestMapping(value = "/getProfilePicture/{username}" , method = RequestMethod.GET) 
    public ResponseEntity<FileSystemResource> getFile(@PathVariable("username") String username) {
    	String fileName = profileService.getFileName(username);
        FileSystemResource resource = new FileSystemResource(new File(BASE_PATH, fileName));
        ResponseEntity<FileSystemResource> responseEntity = new ResponseEntity<FileSystemResource>(resource, HttpStatus.OK);
        return responseEntity;
    }
}
