package com.fexco.carshare.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.simple.JSONObject;

@Entity
@Table(name = "notifications")
public class Notification {
	
	public Notification(){
		
	}
	
	
	public Notification(String senderName, String receiverName, String message, String[] link, String type, Long typeId){
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.message = message;
		this.link = link;
		this.type = type;
		this.typeId = typeId;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String senderName;
	
	private String receiverName;
	
	private String message;
	
	private Date date = new Date();
	
	private boolean seen = false;
	
	private String[] link;
	
	private String type;
	
	private Long typeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	
	public JSONObject toJson(){
		JSONObject json = new JSONObject();
		json.put("id",id);
		json.put("senderName",senderName);
		json.put("receiverName",receiverName);
		json.put("message",message);
		json.put("date",date);
		json.put("seen",seen);
		json.put("link", link);
		json.put("type", type);
		json.put("typeId", typeId);
		return json;
	}


	public String[] getLink() {
		return link;
	}


	public void setLink(String[] link) {
		this.link = link;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Long getTypeId() {
		return typeId;
	}


	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
}
