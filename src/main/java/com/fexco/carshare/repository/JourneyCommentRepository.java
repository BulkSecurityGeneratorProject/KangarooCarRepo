package com.fexco.carshare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fexco.carshare.domain.Journey;
import com.fexco.carshare.domain.JourneyComment;

public interface JourneyCommentRepository extends JpaRepository<JourneyComment, Long>{
	
	@Query("select c from JourneyComment c where c.journey = ?1 and reply = false")
	Page<JourneyComment> findAllByJourneyId(Journey journey, Pageable page);
	
	@Query("select c from JourneyComment c where c.comment = ?1 and reply = true")
	Page<JourneyComment> findAllByComment(JourneyComment comment, Pageable pageable);
}
