package com.guangrui.action.project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.guangrui.action.BaseAction;
import com.guangrui.dto.project.ProjectDTO;
import com.guangrui.dtoutil.project.ProjectDtoUtil;
import com.guangrui.manager.project.ProjectManager;
import com.guangrui.model.project.Project;
import com.guangrui.query.ProjectQuery;
import com.guangrui.util.StringUtil;
import com.guangrui.util.SystemContant;
import com.opensymphony.xwork2.ActionContext;

@Component
@Scope("prototype")
public class ProjectAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8982632805069972047L;
	@Autowired
	private ProjectManager projectManager;
	@Autowired
	private ProjectDtoUtil projectDtoUtil;
	
	private String name;
	
	private int id = 0;
	
	private String startDate;
	
	private String endDate;
	
	private final int pageSize = 20;
	
	private int page = 0;
	
	private String pn;
	
	public String addProject(){
		Project project = null;
		project = this.projectManager.findUniqueByName(name);
		if(project == null){
			project = new Project();
		}
		project.setName(name);
		projectManager.save(project);
		id = project.getId();
		return SUCCESS;
	}
	
	public String listProject(){
		ActionContext context = ActionContext.getContext();
		ProjectQuery query = new ProjectQuery();
		if(StringUtil.isNotBlank(pn)){
			query.setName(pn);
		}
		query.setPageSize(pageSize);
		query.setP(page);
		List<Project> projects = projectManager.queryProjects(query);
		List<ProjectDTO> projectDTOs = projectDtoUtil.getProjectDTOs(projects, query.getStartDate(), query.getEndDate());
		context.put("projectDTOs", projectDTOs);
		context.put("query", query);
		return SUCCESS;
	}
	
	public String viewProject(){
		Project project = null;
		ActionContext context = ActionContext.getContext();
		if(name!=null && !"".equals(name.trim())){
			project = this.projectManager.findUniqueByName(name);
		}else{
			project = this.projectManager.findUniqueById(id);
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(SystemContant.workDateType);
		Date startTime =null;
		Date endTime = null;
		if(StringUtil.isNotBlank(endDate)){
			try {
				endTime = dateFormat.parse(endDate);
			} catch (ParseException e) {
			}
		}
		if(StringUtil.isNotBlank(startDate)){
			try {
				startTime = dateFormat.parse(startDate);
			} catch (ParseException e) {
			}
		}
		if(startTime!=null && endTime !=null && startTime.after(endTime)){
			context.put("error", "开始时间大于结束时间");
			return SUCCESS;
		}
		if(project!=null){
			ProjectDTO projectDTO= projectDtoUtil.getExtraProjectDTO(project, startTime, endTime);
			context.put("projectDTO", projectDTO);
		}
		return SUCCESS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}



	public int getPage() {
		return page;
	}



	public void setPage(int page) {
		this.page = page;
	}

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	

}
