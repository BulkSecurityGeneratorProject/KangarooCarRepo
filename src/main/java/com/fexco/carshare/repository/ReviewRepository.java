package com.fexco.carshare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fexco.carshare.domain.Journey;
import com.fexco.carshare.domain.Review;
import com.fexco.carshare.domain.User;

public interface ReviewRepository extends JpaRepository<Review, Long>{

	Page<Review> findAllByReceiverUser(User user, Pageable page);
	
	@Query("select count(r) from Review r where r.receiverUser = ?")
	int countByUser(User user);
	
	@Query("select sum(r.value) from Review r where r.receiverUser = ?")
	int sumValuesByUser(User user);

	Review findByReceiverUserAndSenderUserAndJourney(User userOther, User user,
			Journey journey);
	
	@Query("SELECT r FROM Review r WHERE r.senderUser  = :sender AND r.receiverUser = :receiver AND r.journey = :journey")
	Review hasReview(@Param("sender")User sender, @Param("receiver") User receiver, @Param("journey")Journey journey);
}
