package com.fexco.carshare.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
@Entity
@Table(name = "makeandmodel")
public class MakeAndModel implements Serializable{

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NotNull
	private Long makeId;
	
	@NotNull
	private String model;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMakeId() {
		return makeId;
	}

	public void setMakeId(Long make) {
		this.makeId = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	public String toString(){
		return String.format("Id: %d\nmake: %d\nmodel: %s", id, makeId, model);
	}
	
}
