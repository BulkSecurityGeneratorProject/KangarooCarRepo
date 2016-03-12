package com.fexco.carshare.service;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fexco.carshare.domain.ProfilePicture;
import com.fexco.carshare.repository.ProfilePictureRepository;
import com.fexco.carshare.repository.UserRepository;
import com.fexco.carshare.web.rest.dto.ProfilePictureDTO;

@Service
@Transactional
public class ProfileService {
	
	@Value("file:/srv/carshare/resources/")
	protected Resource imageDir;
	
	@Inject
	ProfilePictureRepository profilePictureRepository;
	
	@Inject
	private UserRepository userRepository;
	
	public void addProfilePicture(ProfilePictureDTO profilePictureDTO){
		if(userHasProfilePic(profilePictureDTO.getUserName())){
			ProfilePicture profPictureUpdate = getExistingProfilePicture(profilePictureDTO.getUserName());
			dtoToEntity(profPictureUpdate,profilePictureDTO);
			saveFile(profilePictureDTO.getProfilePicture(),profilePictureDTO.getUserName());
			profilePictureRepository.save(profPictureUpdate);
		}
		else{
			ProfilePicture profilePicture = new ProfilePicture();
			dtoToEntity(profilePicture,profilePictureDTO);
			saveFile(profilePictureDTO.getProfilePicture(),profilePictureDTO.getUserName());
			profilePictureRepository.save(profilePicture);
		}
		
	}
	
	public String getFileName(String username) {
		if(userHasProfilePic(username)){
			ProfilePicture profilePicture = getExistingProfilePicture(username);
			return profilePicture.getFileName(); 
		}
		else{
			return "default.jpg";
		}
	}
	
	private ProfilePicture getExistingProfilePicture(String userName) {
		return profilePictureRepository.findByUserId(userRepository.findByLogin(userName).getId());
	}
	
	private void saveFile(MultipartFile profilePicture, String userName) {
		BufferedImage src;
		try {
			String fileExtension =  getFileExtension(profilePicture);
			src = ImageIO.read(new ByteArrayInputStream(profilePicture.getBytes()));
			src = resizeImage(src, 250, 250, true);
			File destination = new File(imageDir.getFile() + File.separator + userName + fileExtension);
			ImageIO.write(src, "png", destination);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void dtoToEntity(ProfilePicture profilePicture,
			ProfilePictureDTO profilePictureDTO) {
		profilePicture.setUser(userRepository.findByLogin(profilePictureDTO.getUserName()));
		profilePicture.setFileName(profilePictureDTO.getUserName()+getFileExtension(profilePictureDTO.getProfilePicture()));
	}
	
	private String getFileExtension(MultipartFile profilePicture) {
		String fileName = profilePicture.getOriginalFilename();
		return profilePicture.getOriginalFilename().substring(fileName.lastIndexOf("."), fileName.length());
	}
	
	public boolean userHasProfilePic(String username){
		ProfilePicture profilePicture = profilePictureRepository.findByUserId(userRepository.findByLogin(username).getId());
		return profilePicture != null ? true:false;
	}
	
	private static BufferedImage resizeImage(BufferedImage before, int targetWidth, int targetHeight,
			boolean higherQuality) {
		
		int w = before.getWidth();
		int h = before.getHeight();
		int xCorner;
		int yCorner;
		int min;
		if(w>h){
			min = h;
			xCorner = (w-h)/2;
			yCorner = 0;
		}
		else if(h>w){
			min = w;
			yCorner =(h-w)/2;
			xCorner = 0;
		}
		else{
			min = w;
			yCorner = 0;
			xCorner = 0;
		}
		
		BufferedImage img = before.getSubimage(xCorner, yCorner, min, min);
		
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		if (ret.getHeight() < targetHeight || ret.getWidth() < targetWidth) {
			higherQuality = false;
		}
		int width, height;
		if (higherQuality) {
			width = img.getWidth();
			height = img.getHeight();
		} else {
			width = targetWidth;
			height = targetHeight;
		}

		do {
			if (higherQuality && width > targetWidth) {
				width /= 2;
				if (width < targetWidth) {
					width = targetWidth;
				}
			}

			if (higherQuality && height > targetHeight) {
				height /= 2;
				if (height < targetHeight) {
					height = targetHeight;
				}
			}

			BufferedImage tmp = new BufferedImage(width, height, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.drawImage(ret, 0, 0, width, height, null);
			g2.dispose();

			ret = tmp;
		} while (width != targetWidth || height != targetHeight);

		return ret;
	}
}
