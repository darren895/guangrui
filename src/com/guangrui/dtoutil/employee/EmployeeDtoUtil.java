package com.guangrui.dtoutil.employee;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guangrui.dto.job.JobDTO;
import com.guangrui.dto.project.ProjectDTO;
import com.guangrui.dto.user.EmployeeDTO;
import com.guangrui.dtoutil.job.JobDtoUtil;
import com.guangrui.dtoutil.project.ProjectDtoUtil;
import com.guangrui.manager.employee.EmployeeManager;
import com.guangrui.manager.job.JobManager;
import com.guangrui.model.job.Job;
import com.guangrui.model.user.Employee;
import com.guangrui.query.JobQuery;
import com.guangrui.query.ProjectQuery;
import com.guangrui.util.ExcelUtil;

@Component
public class EmployeeDtoUtil {
	
	@Autowired
	private EmployeeManager employeeManager;
	@Autowired
	private JobManager jobManager;
	@Autowired
	private JobDtoUtil jobDtoUtil;
	@Autowired
	private ProjectDtoUtil projectDtoUtil;
	
	public EmployeeDTO getExtraEmployeeDTO(Employee employee,Date startDate,Date endDate){
		if(employee==null){
			return null;
		}
		EmployeeDTO employeeDTO = new EmployeeDTO(employee);
		JobQuery jobQuery = new JobQuery();
		jobQuery.setEmployee(employee);
		jobQuery.setStartDate(startDate);
		jobQuery.setEndDate(endDate);
		jobQuery.setPageSize(0);
		List<Job> jobs = jobManager.queryJob(jobQuery);
		List<ProjectDTO> projectDTOs = projectDtoUtil.groupByProject(jobs);
		employeeDTO.setProjectDTOs(projectDTOs);
		double workTime = jobManager.sumWorkTime(jobQuery);
		double overTime = jobManager.sumOverTime(jobQuery);
		double weekendTime = jobManager.sumWeekendTime(jobQuery);
		employeeDTO.setTime(workTime);
		employeeDTO.setOverTime(overTime);
		employeeDTO.setWeekendTime(weekendTime);
		return employeeDTO;
	}
	
	public EmployeeDTO getEmployeeDTO(Employee employee,Date startDate,Date endDate){
		if(employee==null){
			return null;
		}
		EmployeeDTO employeeDTO = new EmployeeDTO(employee);
		JobQuery jobQuery = new JobQuery();
		jobQuery.setEmployee(employee);
		jobQuery.setStartDate(startDate);
		jobQuery.setEndDate(endDate);
		jobQuery.setPageSize(0);
//		List<Job> jobs = jobManager.queryJob(jobQuery);
//		List<ProjectDTO> projectDTOs = projectDtoUtil.groupByProject(jobs);
//		employeeDTO.setProjectDTOs(projectDTOs);
		double workTime = jobManager.sumWorkTime(jobQuery);
		double overTime = jobManager.sumOverTime(jobQuery);
		double weekendTime = jobManager.sumWeekendTime(jobQuery);
		employeeDTO.setTime(workTime);
		employeeDTO.setOverTime(overTime);
		employeeDTO.setWeekendTime(weekendTime);
		return employeeDTO;
	}
	
	public List<EmployeeDTO> groupByEmployee(List<Job> jobs){
		if(jobs!=null && !jobs.isEmpty()){
			List<EmployeeDTO> employeeDTOs = new ArrayList<EmployeeDTO>();
			Map<Integer, EmployeeDTO> map = new HashMap<Integer, EmployeeDTO>();
			for (Job job : jobs) {
				EmployeeDTO employeeDTO = map.get(job.getEmployee().getId());
				if(employeeDTO == null){
					employeeDTO = new EmployeeDTO();
					employeeDTO.setId(job.getEmployee().getId());
					employeeDTO.setName(job.getEmployee().getName());
					employeeDTOs.add(employeeDTO);
					map.put(employeeDTO.getId(), employeeDTO);
				}
				employeeDTO.setTime(employeeDTO.getTime()+job.getWorkTime());
				employeeDTO.setOverTime(employeeDTO.getOverTime()+job.getOverTime());
				employeeDTO.setWeekendTime(employeeDTO.getWeekendTime()+job.getWeekendTime());
			}
			return employeeDTOs;
		}else{
			return null;
		}
	}
	
	public ByteArrayOutputStream getEmployeeExcel(Employee employee,Date startDate,Date endDate){
		EmployeeDTO employeeDTO = this.getExtraEmployeeDTO(employee, startDate, endDate);
		ByteArrayOutputStream os = ExcelUtil.getEmployeeExcel(employeeDTO);
		return os;
	}
		
	
	public EmployeeDTO getSimpleEmployeeDTO(Employee employee){
		if(employee==null){
			return null;
		}
		EmployeeDTO employeeDTO = new EmployeeDTO(employee);
		
//		List<Job> jobs = jobManager.queryJob(jobQuery);
		return employeeDTO;
	}
	
	public List<EmployeeDTO> getEmployeeDTOList(List<Employee> employees){
		if(employees==null || employees.isEmpty()){
			return null;
		}
		List<EmployeeDTO> employeeDTOs = new ArrayList<EmployeeDTO>();
		for (Employee employee : employees) {
			EmployeeDTO employeeDTO = getEmployeeDTO(employee, null, null);
			employeeDTOs.add(employeeDTO);
		}
		return employeeDTOs;
	}

}
