package com.fexco.carshare.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.fexco.carshare.domain.JourneyRequest;

public interface JourneyRequestRepository extends PagingAndSortingRepository<JourneyRequest, Long>{

	public Page<JourneyRequest> findAll(Pageable pageable);
	Page<JourneyRequest> findAllByUserId(Long id, Pageable page);
	Iterable<JourneyRequest> findAll(Sort sort);
	Iterable<JourneyRequest> findAllByUserId(Long id, Sort sort);
	Iterable<JourneyRequest> findAllBySource(String source, Sort sort);
	
	@Query("SELECT j FROM JourneyRequest j WHERE j.user.id IN (SELECT DISTINCT u.id FROM User u WHERE u.login LIKE CONCAT ('%',:userName,'%'))"
			+ "AND j.source LIKE CONCAT"
			+ " ('%',:source,'%') AND j.destination LIKE CONCAT ('%',:destination,'%') AND j.date LIKE :date ORDER BY j.id DESC")
	Page<JourneyRequest> findAllByUserandSourceAndDestinationAndDateQuery(@Param("userName") String userName,
			@Param("source") String source, @Param("destination") String destination, @Param("date") Date date, Pageable page);
	
	@Query("SELECT j FROM JourneyRequest j WHERE j.user.id IN (SELECT DISTINCT u.id FROM User u WHERE u.login LIKE CONCAT ('%',:userName,'%'))"
			+ "AND j.source LIKE CONCAT"
			+ " ('%',:source,'%') AND j.destination LIKE CONCAT ('%',:destination,'%') ORDER BY j.id DESC")
	Page<JourneyRequest> findAllByUserandSourceAndDestinationQuery(@Param("userName") String userName,
			@Param("source") String source, @Param("destination") String destination, Pageable page);
}

