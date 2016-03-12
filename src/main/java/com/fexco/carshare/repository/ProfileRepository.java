package com.fexco.carshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fexco.carshare.domain.Profile;
import com.fexco.carshare.domain.User;

public interface ProfileRepository extends JpaRepository<Profile, Long>{
	Profile findOneByUser(User user);
}
