package com.fexco.carshare.web.rest;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.fexco.carshare.domain.Notification;
import com.fexco.carshare.repository.NotificationRepository;
import com.fexco.carshare.service.NotificationService;
import com.fexco.carshare.web.rest.dto.Greeting;
import com.fexco.carshare.web.rest.dto.HelloMessage;

@Controller
public class NotifyController {
	
	@Inject
	NotificationService notificationService;
	
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
    public NotifyController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
	
	@Inject
	NotificationRepository notificationRepository;
	
	@MessageMapping("/notifications/{username}")
    public void greeting(HelloMessage message, @DestinationVariable("username") String username) throws Exception {
		simpMessagingTemplate.convertAndSend("/user/" + username + "/get/notifications/", new Greeting("Hello, " + message.getName() + "!"));
    }
	
	public void sendNotification(String receiverName, Notification payload){
		if(!payload.getReceiverName().equals(payload.getSenderName())){
			Notification notification = notificationRepository.save(payload);
			simpMessagingTemplate.convertAndSend("/user/" + receiverName + "/get/notifications/", notification.toJson());
		}
	}
	
}
