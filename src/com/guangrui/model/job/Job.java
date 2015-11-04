package com.guangrui.model.job;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

import com.guangrui.model.BaseModel;
import com.guangrui.model.project.Project;
import com.guangrui.model.user.Employee;

@Entity(name="job")
public class Job extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 469371835662674441L;
	
	private int id;
	
	private double workTime;
	
	private Employee employee;
	
	private Project project;
	
	private Date workDate;
	
	private double overTime;
	
	private double weekendTime;

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@ForeignKey(name="null")
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@ForeignKey(name="null")
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Temporal(TemporalType.DATE)
	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	
	@Column(columnDefinition = "double default 0")
	public double getOverTime() {
		return overTime;
	}
	
	public void setOverTime(double overTime) {
		this.overTime = overTime;
	}
	
	@Column(columnDefinition = "double default 0")
	public double getWorkTime() {
		return workTime;
	}

	public void setWorkTime(double workTime) {
		this.workTime = workTime;
	}

	@Column(columnDefinition = "double default 0")
	public double getWeekendTime() {
		return weekendTime;
	}

	public void setWeekendTime(double weekendTime) {
		this.weekendTime = weekendTime;
	}
	
	

	
}
