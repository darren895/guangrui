package com.guangrui.query;

import java.io.Serializable;
import java.util.Date;

import com.guangrui.util.Page;

public class ProjectQuery extends BaseQuery implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7854815742209493097L;

	private Date startDate;
	
	private Date endDate;
	
	private String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
