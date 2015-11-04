package com.guangrui.dto.project;

import java.io.Serializable;
import java.util.List;

import com.guangrui.dto.job.JobDTO;
import com.guangrui.dto.user.EmployeeDTO;
import com.guangrui.model.project.Project;

public class ProjectDTO implements Serializable {
	
	private int id;
	
	private String name;
	
	private double time = 0;
	
	private double overTime = 0;
	
	private double weekendTime = 0;
	
	private List<JobDTO> jobDTOs;
	
	private List<EmployeeDTO> employeeDTOs;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1227988706980305472L;

	public ProjectDTO() {
		
	}
	
	public ProjectDTO(Project project){
		id = project.getId();
		name = project.getName();
	}

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

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getOverTime() {
		return overTime;
	}

	public void setOverTime(double overTime) {
		this.overTime = overTime;
	}

	public List<JobDTO> getJobDTOs() {
		return jobDTOs;
	}

	public void setJobDTOs(List<JobDTO> jobDTOs) {
		this.jobDTOs = jobDTOs;
		if(jobDTOs!=null && !jobDTOs.isEmpty()){
			double time = 0;
			double overTime = 0;
			for (JobDTO jobDTO : jobDTOs) {
				time +=jobDTO.getWorkTime();
				overTime += jobDTO.getOverTime();
			}
			this.time = time;
			this.overTime = overTime;
		}
	}

	public double getWeekendTime() {
		return weekendTime;
	}

	public void setWeekendTime(double weekendTime) {
		this.weekendTime = weekendTime;
	}

	public List<EmployeeDTO> getEmployeeDTOs() {
		return employeeDTOs;
	}

	public void setEmployeeDTOs(List<EmployeeDTO> employeeDTOs) {
		this.employeeDTOs = employeeDTOs;
	}


}
