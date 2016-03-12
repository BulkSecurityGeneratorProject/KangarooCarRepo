package com.fexco.carshare.web.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fexco.carshare.domain.Notification;
import com.fexco.carshare.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
	
	@Inject
	NotificationService notificationService;
	
	@RequestMapping(value = "/allUnseen/{username}", method = RequestMethod.GET)
	public List<Notification> getUnseen(@PathVariable("username") String username){
		
		return notificationService.allUnseenByUsername(username);
	}
	
	@RequestMapping(value = "/allNotifications/{username}", method = RequestMethod.GET)
	public Page<Notification> getAllNotifications(@PathVariable("username") String username, Pageable page){
		
		return notificationService.allNotificationsByUsername(username,page);
	}
	
	@RequestMapping(value = "/markSeen/{username}", method = RequestMethod.GET)
	public void markSeen(@PathVariable("username") String username){
		
		notificationService.markSeen(username);
	}
}
