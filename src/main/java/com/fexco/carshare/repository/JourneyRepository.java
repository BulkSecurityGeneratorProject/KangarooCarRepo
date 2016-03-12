package com.fexco.carshare.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fexco.carshare.domain.Journey;

public interface JourneyRepository extends JpaRepository<Journey, Long>
{	
	public Page<Journey> findAll(Pageable pageable);
	List<Journey> findAll(Sort sort);
	Page<Journey> findAllByUserId(Long userId, Pageable pageable);
	Iterable<Journey> findAllBySource(String source, Sort sort);
	
	@Query("SELECT j FROM Journey j WHERE j.user.id IN (SELECT DISTINCT u.id FROM User u WHERE u.login LIKE CONCAT ('%',:userName,'%'))"
			+ "AND j.source LIKE CONCAT"
			+ " ('%',:source,'%') AND j.destination LIKE CONCAT ('%',:destination,'%') AND j.date LIKE :date AND j.date >= :yesterday ORDER BY j.id DESC")
	Page<Journey> findAllByUserandSourceAndDestinationAndDateQuery(@Param("userName") String userName,
			@Param("source") String source, @Param("destination") String destination, @Param("date") Date date, Pageable page, @Param("yesterday") Date yesterday);
	
	@Query("SELECT j FROM Journey j WHERE j.user.id IN (SELECT DISTINCT u.id FROM User u WHERE u.login LIKE CONCAT ('%',:userName,'%'))"
			+ "AND j.source LIKE CONCAT"
			+ " ('%',:source,'%') AND j.destination LIKE CONCAT ('%',:destination,'%') AND j.date >= :yesterday ORDER BY j.id DESC")
	Page<Journey> findAllByUserandSourceAndDestinationQuery(@Param("userName") String userName,
			@Param("source") String source, @Param("destination") String destination, Pageable page, @Param("yesterday") Date yesterday);
	
	@Query("SELECT j FROM Journey j WHERE j.date >= :date")
	public Page<Journey> findAllRecent(Pageable page, @Param("date") Date yesterday);
}
