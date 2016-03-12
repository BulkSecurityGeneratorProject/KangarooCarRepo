package com.fexco.carshare.repository;

import java.util.ArrayList;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fexco.carshare.domain.JourneyPassenger;
import com.fexco.carshare.domain.User;

public interface JourneyPassengerRepository extends JpaRepository<JourneyPassenger, Long>{
	
	@Query("select j from JourneyPassenger j, JourneyPassenger j2 where j.journey.id = j2.journey.id AND j.user = ?1 AND j2.user = ?2")
	ArrayList<JourneyPassenger> findCommon(User me, User other, Sort sort);
	
	@Query("select j from JourneyPassenger j where j.journey.id = ?1 AND j.user.login != ?2")
	ArrayList<JourneyPassenger> getPassengers(Long journeyId, String driverName);

}
