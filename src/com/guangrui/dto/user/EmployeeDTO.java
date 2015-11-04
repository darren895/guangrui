package com.guangrui.dto.user;

import java.io.Serializable;
import java.util.List;

import com.guangrui.dto.project.ProjectDTO;
import com.guangrui.model.user.Employee;

public class EmployeeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7076082681483974806L;
	
	private int id;
	
	private String name;
	
	private double time = 0;
	
	private double overTime = 0;
	
	private double weekendTime = 0;
	
	private List<ProjectDTO> projectDTOs;
	
	public EmployeeDTO() {		
	}
	
	public EmployeeDTO(Employee employee){
		id = employee.getId();
		name = employee.getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTime() {
		return time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getOverTime() {
		return overTime;
	}

	public void setOverTime(double overTime) {
		this.overTime = overTime;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getWeekendTime() {
		return weekendTime;
	}

	public void setWeekendTime(double weekendTime) {
		this.weekendTime = weekendTime;
	}

	public List<ProjectDTO> getProjectDTOs() {
		return projectDTOs;
	}

	public void setProjectDTOs(List<ProjectDTO> projectDTOs) {
		this.projectDTOs = projectDTOs;
	}
	
	

}
