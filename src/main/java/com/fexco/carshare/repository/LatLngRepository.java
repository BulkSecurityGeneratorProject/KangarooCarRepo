package com.fexco.carshare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fexco.carshare.domain.LatLng;

public interface LatLngRepository extends JpaRepository<LatLng, Long>{
	
	List<LatLng> findAllByJourneyId(Long journeyId);
}
