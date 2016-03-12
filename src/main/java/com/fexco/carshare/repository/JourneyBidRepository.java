package com.fexco.carshare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.fexco.carshare.domain.Journey;
import com.fexco.carshare.domain.JourneyBid;
import com.fexco.carshare.domain.User;

public interface JourneyBidRepository extends CrudRepository<JourneyBid,Long>{
	Iterable<JourneyBid> findAllByJourney(Journey journey, Sort sort);
	Iterable<JourneyBid> findAllByUserAndJourney(User user, Journey journey);
	Long countByJourneyIdAndStatus(Long journeyId, String status);
	Page<JourneyBid> findAllByUser(User user, Pageable page);
}
