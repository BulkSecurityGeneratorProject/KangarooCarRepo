package com.fexco.carshare.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.fexco.carshare.domain.Notification;

public interface NotificationRepository extends CrudRepository<Notification, Long>{

	List<Notification> findAllByReceiverNameAndSeen(String receiverName, boolean seen,
			Sort sort);
	Page<Notification> findAllByReceiverName(String username, Pageable page);
}
