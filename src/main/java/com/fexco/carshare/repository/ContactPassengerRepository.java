package com.fexco.carshare.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.fexco.carshare.domain.ContactPassenger;
import com.fexco.carshare.domain.JourneyRequest;

public interface ContactPassengerRepository extends CrudRepository<ContactPassenger, Long>{
	Iterable<ContactPassenger> findAllByJourneyRequest(JourneyRequest journeyRequest, Sort sort);
}