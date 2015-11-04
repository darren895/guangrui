package com.guangrui.dtoutil.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guangrui.dto.project.ProjectDTO;
import com.guangrui.dto.user.EmployeeDTO;
import com.guangrui.dtoutil.employee.EmployeeDtoUtil;
import com.guangrui.dtoutil.job.JobDtoUtil;
import com.guangrui.manager.job.JobManager;
import com.guangrui.model.job.Job;
import com.guangrui.model.project.Project;
import com.guangrui.query.JobQuery;

@Component
public class ProjectDtoUtil {
	
	@Autowired
	private JobManager jobManager;
	@Autowired
	private JobDtoUtil jobDtoUtil;
	@Autowired
	private EmployeeDtoUtil employeeDtoUtil;
	
	public ProjectDTO getExtraProjectDTO(Project project,Date startDate,Date endDate){
		if(project == null || project.getId() ==0){
			return null;
		}
		try {
			ProjectDTO projectDTO = new ProjectDTO(project);
			JobQuery jobQuery = new JobQuery();
			jobQuery.setProject(project);
			jobQuery.setStartDate(startDate);
			jobQuery.setEndDate(endDate);
			jobQuery.setPageSize(0);
			List<Job> jobs = jobManager.queryJob(jobQuery);
			List<EmployeeDTO> employeeDTOs = employeeDtoUtil.groupByEmployee(jobs);
			double worktime = jobManager.sumWorkTime(jobQuery);
			double overtime = jobManager.sumOverTime(jobQuery);
			double weekendTime = jobManager.sumWeekendTime(jobQuery);
			projectDTO.setTime(worktime);
			projectDTO.setOverTime(overtime);
			projectDTO.setWeekendTime(weekendTime);
			projectDTO.setEmployeeDTOs(employeeDTOs);
			return projectDTO;
		} catch (Exception e) {
			return null;
		}
	}
	
	public ProjectDTO getProjectDTO(Project project,Date startDate,Date endDate){
		if(project == null || project.getId() ==0){
			return null;
		}
		try {
			ProjectDTO projectDTO = new ProjectDTO(project);
			JobQuery jobQuery = new JobQuery();
			jobQuery.setProject(project);
			jobQuery.setStartDate(startDate);
			jobQuery.setEndDate(endDate);
			jobQuery.setPageSize(0);
			double worktime = jobManager.sumWorkTime(jobQuery);
			double overtime = jobManager.sumOverTime(jobQuery);
			double weekendTime = jobManager.sumWeekendTime(jobQuery);
			projectDTO.setTime(worktime);
			projectDTO.setOverTime(overtime);
			projectDTO.setWeekendTime(weekendTime);
			return projectDTO;
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<ProjectDTO> getProjectDTOs(List<Project> projects,Date startDate, Date endDate){
		if(projects == null || projects.isEmpty()){
			return null;
		}
		List<ProjectDTO> projectDTOs = new ArrayList<ProjectDTO>();
		for (Project project : projects) {
			ProjectDTO projectDTO = getProjectDTO(project, startDate, endDate);
			projectDTOs.add(projectDTO);
		}
		return projectDTOs;	
	}
	
	public List<ProjectDTO> groupByProject(List<Job> jobs){
		if(jobs!=null && !jobs.isEmpty()){
			List<ProjectDTO> projectDTOs = new ArrayList<ProjectDTO>();
			Map<Integer, ProjectDTO> map = new HashMap<Integer, ProjectDTO>();
			for (Job job : jobs) {
				ProjectDTO projectDTO = map.get(job.getProject().getId());
				if(projectDTO == null){
					projectDTO = new ProjectDTO();
					projectDTO.setId(job.getProject().getId());
					projectDTO.setName(job.getProject().getName());
					projectDTOs.add(projectDTO);
					map.put(projectDTO.getId(), projectDTO);
				}
				projectDTO.setTime(projectDTO.getTime()+job.getWorkTime());
				projectDTO.setOverTime(projectDTO.getOverTime()+job.getOverTime());
				projectDTO.setWeekendTime(projectDTO.getWeekendTime()+job.getWeekendTime());
			}
			return projectDTOs;
		}else{
			return null;
		}
	}
}
