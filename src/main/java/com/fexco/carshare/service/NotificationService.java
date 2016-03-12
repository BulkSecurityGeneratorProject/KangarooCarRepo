package com.fexco.carshare.service;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fexco.carshare.domain.Notification;
import com.fexco.carshare.repository.NotificationRepository;

@Service
@Transactional
public class NotificationService {
	
	@Inject
	NotificationRepository notificationRepository;

	public List<Notification> allUnseenByUsername(String username) {
		List<Notification> n = notificationRepository.findAllByReceiverNameAndSeen(username, false,new Sort(Sort.Direction.DESC, "id"));
		return n;
	}

	public void markSeen(String username) {
		List<Notification> notifications = notificationRepository.findAllByReceiverNameAndSeen(username, false,new Sort(Sort.Direction.DESC, "id"));
		for(Notification n : notifications){
			n.setSeen(true);
			notificationRepository.save(n);
		}
	}

	public Page<Notification> allNotificationsByUsername(String username,
			Pageable page) {
		return notificationRepository.findAllByReceiverName(username, page);
	} 

}
