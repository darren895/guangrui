package com.guangrui.action.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.guangrui.action.BaseAction;
import com.guangrui.dto.job.JobDTO;
import com.guangrui.dto.user.EmployeeDTO;
import com.guangrui.dtoutil.employee.EmployeeDtoUtil;
import com.guangrui.dtoutil.job.JobDtoUtil;
import com.guangrui.manager.employee.EmployeeManager;
import com.guangrui.manager.job.JobManager;
import com.guangrui.manager.project.ProjectManager;
import com.guangrui.model.job.Job;
import com.guangrui.model.project.Project;
import com.guangrui.model.user.Employee;
import com.guangrui.query.EmployeeQuery;
import com.guangrui.query.JobQuery;
import com.guangrui.util.StringUtil;
import com.guangrui.util.SystemContant;
import com.opensymphony.xwork2.ActionContext;

@Component
@Scope("prototype")
public class JobAction extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3096006452885436592L;
	@Autowired
	private JobManager jobManager;
	@Autowired
	private EmployeeManager employeeManager;
	@Autowired
	private ProjectManager projectManager;
	@Autowired
	private JobDtoUtil jobDtoUtil;
	@Autowired
	private EmployeeDtoUtil employeeDtoUtil;
	
	private JobDTO jobDTO;

	private String e;//employeeName
	
	private String pn;//projectName;
	
	private String startDate;
	
	private String endDate;
	
	private String employeeId;
	
	private int all = 0;//是否分页 0分页 1全部
	
	private int id;
	
	private final int pageSize =  20;
	
	private int p = 1;
	
	private List<JobDTO> jobDTOs;
	
	private JSONObject json = new JSONObject();
	public String addJob(){
		request.getParameterMap();
		Job job = new Job();
		Project project = this.projectManager.findUniqueByName(jobDTO.getProjectName());
		if(project ==null){
			json.put("status", false);
			json.put("error", "项目不存在");
			return SUCCESS;
		}
		Employee employee = this.employeeManager.findUniqueByName(jobDTO.getEmployeeName());
		if(employee == null){
			json.put("status", false);
			json.put("error", "员工不存在");
			return SUCCESS;
		}
		job.setEmployee(employee);
		job.setProject(project);
		job.setWorkTime(jobDTO.getWorkTime());
		job.setOverTime(jobDTO.getOverTime());
		job.setWeekendTime(jobDTO.getWeekendTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat(SystemContant.workDateType);
		if(jobDTO.getDate()!=null){
			try {
				Date date = dateFormat.parse(jobDTO.getDate());
				job.setWorkDate(date);
			} catch (ParseException e) {
				json.put("status", false);
				json.put("error", "工作日期没输入");
				return SUCCESS;
			}
		}else{
			json.put("status", false);
			json.put("error", "工作日期没输入");
			return SUCCESS;
		}
		this.jobManager.save(job);
		if(job.getId()>0){
			json.put("status", true);
			json.put("id", job.getId());
		}else{
			json.put("status", false);
			json.put("error", "保存失败，请重试");
		}
		return SUCCESS;
	}
	
	public String viewJob(){
		Job job = jobManager.findUniqueById(id);
		jobDTO = jobDtoUtil.getJobDTO(job);
		return SUCCESS;
	}
	
	public String listJob(){
		ActionContext context = ActionContext.getContext();
		JobQuery jobQuery = new JobQuery();
		if(StringUtil.isNotBlank(e)){
			Employee employee = employeeManager.findUniqueByName(e);
			if(employee==null){
				jobQuery.setTotal(0);
				context.put("query", jobQuery);
				return SUCCESS;
			}
			jobQuery.setEmployee(employee);
		}
		if(StringUtil.isNotBlank(pn)){
			Project project = projectManager.findUniqueByName(pn);
			if(project == null){
				jobQuery.setTotal(0);
				context.put("query", jobQuery);
				return SUCCESS;
			}
			jobQuery.setProject(project);
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(SystemContant.workDateType);
		if(StringUtil.isNotBlank(endDate)){
			Date endTime = null;
			try {
				endTime = dateFormat.parse(endDate);
			} catch (ParseException e1) {
				endTime = new Date();
			}
			jobQuery.setEndDate(endTime);
		}else{
			jobQuery.setEndDate(new Date());
		}
		Date startTime = null;
		if(StringUtil.isNotBlank(startDate)){
			try {
				startTime = dateFormat.parse(startDate);
			} catch (ParseException e1) {
			}
		}
		if(startTime == null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(jobQuery.getEndDate());
			calendar.add(Calendar.DATE, -15);
			startTime = calendar.getTime();
			
		}
		jobQuery.setStartDate(startTime);
		if(all==1){
			jobQuery.setPageSize(0);
		}else{
			jobQuery.setPageSize(pageSize);
			jobQuery.setP(p);
		}
		List<Job> jobs = jobManager.queryJob(jobQuery);
		List<JobDTO> jobDTOs = jobDtoUtil.getJobDTOList(jobs);
		double workTime = jobManager.sumWorkTime(jobQuery);
		double overTime = jobManager.sumOverTime(jobQuery);
		context.put("overTime", overTime);
		context.put("workTime", workTime);
		context.put("jobDTOs", jobDTOs);
		context.put("query", jobQuery);
		startDate = dateFormat.format(jobQuery.getStartDate());
		endDate = dateFormat.format(jobQuery.getEndDate());
		return SUCCESS;
	}
	
	/**
	 * 批量增加工作
	 * @return
	 */
	public String addJobs(){
		if(this.jobDTOs!=null && !jobDTOs.isEmpty()){
			String message = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat(SystemContant.workDateType);
			Map<String, Project> projectMap = new HashMap<String, Project>();
			Map<Integer, Employee> employeeMap = new HashMap<Integer, Employee>();
			for (JobDTO jobDTO : jobDTOs) {
				if(jobDTO.getWorkTime() == 0 && jobDTO.getOverTime() == 0 && jobDTO.getWeekendTime() == 0){
					message += "项目:"+jobDTO.getProjectName()+"工作时间都为0;";
					continue;
				}
				Job job = new Job();
				Employee employee = null;
				if(employeeMap.get(jobDTO.getEmployeeId())==null){
					employee = this.employeeManager.findUniqueById(jobDTO.getEmployeeId());
					if(employee!=null){
						employeeMap.put(jobDTO.getEmployeeId(), employee);
						job.setEmployee(employee);
					}else{
						message ="用户不存在";
						break;
					}
				}else{
					employee = employeeMap.get(jobDTO.getEmployeeId());
					job.setEmployee(employee);
				}
				Project project = null;
				if(projectMap.get(jobDTO.getProjectName())==null){
					project = projectManager.findUniqueByName(jobDTO.getProjectName().trim());
					if(project==null){
						message += jobDTO.getProjectName()+"不存在;";
						continue;
					}else{
						projectMap.put(jobDTO.getProjectName(), project);
						job.setProject(project);
					}
				}else{
					project = projectMap.get(jobDTO.getProjectName());
					job.setProject(project);
				}
				String time = jobDTO.getDate();
				try {
					Date date = dateFormat.parse(time);
					job.setWorkDate(date);
				} catch (ParseException e) {
					message += time+"日期的工作添加失败;";
					continue;
				}
				job.setWorkTime(jobDTO.getWorkTime());
				job.setOverTime(jobDTO.getOverTime());
				job.setWeekendTime(jobDTO.getWeekendTime());
				jobManager.save(job);
				if(job.getId()==0){
					message += jobDTO.getProjectName()+"项目有工作未提交成功，请检查;";
				}
			}
			if(message.equals("")){
				json.put("status", true);
			}else{
				json.put("status", false);
				json.put("message", message);
			}
			
		}
		return SUCCESS;
	}
	
	public String gridJob(){
		ActionContext context = ActionContext.getContext();
		List<Employee> employees = this.employeeManager.getAllEmployees();
		List<EmployeeDTO> employeeDTOs = this.employeeDtoUtil.getEmployeeDTOList(employees);
		context.put("employeeDTOs", employeeDTOs);
		SimpleDateFormat dateFormat = new SimpleDateFormat(SystemContant.workDateType);
		String endDate = dateFormat.format(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -15);
		String startDate = dateFormat.format(calendar.getTime());
		context.put("endDate", endDate);
		context.put("startDate", startDate);
		return SUCCESS;
	}
	
	public String viewGridJob(){
		ActionContext context = ActionContext.getContext();
		if(!StringUtil.isNotBlank(employeeId)){
			return SUCCESS;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(SystemContant.workDateType);
		String[] ids = this.employeeId.split(",");
		List<Integer> idArray = new ArrayList<Integer>();
		List<Employee> queryEmployees = new ArrayList<Employee>();
		for (String id : ids) {
			Employee employee = new Employee();
			employee.setId(Integer.parseInt(id.trim()));
			queryEmployees.add(employee);
			idArray.add(employee.getId());
		}
		JobQuery jobQuery = new JobQuery();
		jobQuery.setPageSize(0);
		if(StringUtil.isNotBlank(endDate)){
			try {
				Date date = dateFormat.parse(endDate);
				jobQuery.setEndDate(date);
			} catch (ParseException e) {
				jobQuery.setEndDate(new Date());
			}
		}else{
			jobQuery.setEndDate(new Date());
		}
		if(StringUtil.isNotBlank(startDate)){
			try {
				Date date = dateFormat.parse(startDate);
				jobQuery.setStartDate(date);
			} catch (ParseException e) {
			}
		}
		if(jobQuery.getStartDate()==null){
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -15);
			jobQuery.setStartDate(calendar.getTime());
		}
		jobQuery.setEmployeeIds(queryEmployees);
		List<Job> jobs = jobManager.queryJob(jobQuery);
		EmployeeQuery employeeQuery = new EmployeeQuery();
		employeeQuery.setIds(idArray);
		employeeQuery.setPageSize(0);
		List<Employee> employees = this.employeeManager.queryEmployees(employeeQuery);
		List<EmployeeDTO> employeeDTOs = new ArrayList<EmployeeDTO>();
		Map<String, List<JobDTO>> tempMap = new HashMap<String, List<JobDTO>>();
		Map<Integer, Map<String, List<JobDTO>>> jobMap = new HashMap<Integer, Map<String,List<JobDTO>>>();
		for (Job job : jobs) {
			JobDTO jobDTO = jobDtoUtil.getJobDTO(job);
			if(tempMap.get(jobDTO.getEmployeeName())==null){
				tempMap.put(jobDTO.getEmployeeName(), new ArrayList<JobDTO>());
			}
			tempMap.get(jobDTO.getEmployeeName()).add(jobDTO);
		}
		for (Employee employee : employees) {
			EmployeeDTO employeeDTO = this.employeeDtoUtil.getSimpleEmployeeDTO(employee);
			employeeDTOs.add(employeeDTO);
			if (jobMap.get(employee.getId())==null) {
				jobMap.put(employee.getId(), new HashMap<String, List<JobDTO>>());
			}
			List<JobDTO> jobDTOs = tempMap.get(employee.getName());
			if(jobDTOs!=null){
				for (JobDTO jobDTO : jobDTOs) {
					if (jobMap.get(employee.getId()).get(jobDTO.getDate())==null) {
						jobMap.get(employee.getId()).put(jobDTO.getDate(), new ArrayList<JobDTO>());
					}
					jobMap.get(employee.getId()).get(jobDTO.getDate()).add(jobDTO);
				}
			}
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(jobQuery.getStartDate());
		List<String> dates = new ArrayList<String>();
		while (calendar.getTime().before(jobQuery.getEndDate()) || calendar.getTime().equals(jobQuery.getEndDate())) {
			Date date = calendar.getTime();
			String time = dateFormat.format(date);
			dates.add(time);
			calendar.add(Calendar.DATE, 1);
		}
		context.put("dates", dates);
		context.put("employeeDTOs", employeeDTOs);
		context.put("jobMap", jobMap);
		return SUCCESS;
	}
	
	public String removeJob(){
		json.put("status", false);
		try {
			this.jobManager.removeJobById(id);
			json.put("status", true);
		} catch (Exception e) {
		}
		return SUCCESS;
	}
	
	public JobDTO getJobDTO() {
		return jobDTO;
	}
	public void setJobDTO(JobDTO jobDTO) {
		this.jobDTO = jobDTO;
	}
	public JSONObject getJson() {
		return json;
	}
	public void setJson(JSONObject json) {
		this.json = json;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
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

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public List<JobDTO> getJobDTOs() {
		return jobDTOs;
	}

	public void setJobDTOs(List<JobDTO> jobDTOs) {
		this.jobDTOs = jobDTOs;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	

	
	
}
