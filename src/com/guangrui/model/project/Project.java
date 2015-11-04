package com.guangrui.model.project;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.guangrui.model.BaseModel;

@Entity(name="Project")
public class Project extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7188555543143329994L;
	
	private int id;
	
	private String name;

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	
	
}
