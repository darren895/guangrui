package com.guangrui.query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.guangrui.model.project.Project;
import com.guangrui.model.user.Employee;
import com.guangrui.util.Page;

public class JobQuery extends BaseQuery implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2336079060588138679L;

	private Employee employee;
	
	private Project project;
	
	private Date startDate;
	
	private Date endDate;
	
	private List<Employee> employeeIds;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Employee> getEmployeeIds() {
		return employeeIds;
	}

	public void setEmployeeIds(List<Employee> employeeIds) {
		this.employeeIds = employeeIds;
	}

	
	

}
