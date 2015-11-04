package com.guangrui.dto.job;

import java.io.Serializable;

public class JobDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1793867289900805546L;

	private String date;
	
	private String employeeName;
	
	private int employeeId;
	
	private int id ;
	
	private String projectName;
	
	private double workTime;
	
	private double overTime;
	
	private double weekendTime;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public double getWorkTime() {
		return workTime;
	}

	public void setWorkTime(double workTime) {
		this.workTime = workTime;
	}

	public double getOverTime() {
		return overTime;
	}

	public void setOverTime(double overTime) {
		this.overTime = overTime;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getWeekendTime() {
		return weekendTime;
	}

	public void setWeekendTime(double weekendTime) {
		this.weekendTime = weekendTime;
	}
	
	

}
