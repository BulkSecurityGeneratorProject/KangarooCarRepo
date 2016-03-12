package com.fexco.carshare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fexco.carshare.domain.Comment;
import com.fexco.carshare.domain.User;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	@Query("select c from Comment c where c.receiverUser = ?1 and reply = false")
	Page<Comment> findAllByReceiverUser(User user, Pageable pageable);
	
	@Query("select c from Comment c where c.comment = ?1 and reply = true")
	Page<Comment> findAllByComment(Comment comment, Pageable pageable);

}