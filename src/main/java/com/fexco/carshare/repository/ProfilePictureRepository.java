package com.fexco.carshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fexco.carshare.domain.ProfilePicture;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long>{
	public ProfilePicture findByUserId(Long userId);
}

