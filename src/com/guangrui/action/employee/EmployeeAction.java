package com.guangrui.action.employee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.guangrui.action.BaseAction;
import com.guangrui.dto.user.EmployeeDTO;
import com.guangrui.dtoutil.employee.EmployeeDtoUtil;
import com.guangrui.manager.employee.EmployeeManager;
import com.guangrui.model.user.Employee;
import com.guangrui.query.EmployeeQuery;
import com.guangrui.util.StringUtil;
import com.guangrui.util.SystemContant;
import com.opensymphony.xwork2.ActionContext;

@Component(value = "employeeAction")
@Scope("prototype")
public class EmployeeAction extends BaseAction {
	
	private String name;
	
	private int id;
	
	private String startDate;
	
	private String endDate;
	
	private String time;
	
	private boolean add;

	private final int pageSize = 10;
	
	private int p = 1;
	
	private JSONObject json = new JSONObject();
	
	private InputStream inputStream;
	
	private String fileName;
	
	@Autowired
	private EmployeeManager employeeManager;
	@Autowired
	private EmployeeDtoUtil employeeDtoUtil;

	/**
	 * 
	 */
	private static final long serialVersionUID = -3618418965455115583L;
	
	public String addEmployee(){
		Employee employee = null;
		employee = this.employeeManager.findUniqueByName(name);
		if(employee == null){
			employee = new Employee();
		}
		employee.setName(name);
		employeeManager.save(employee);
		id = employee.getId();
		return SUCCESS;
	}
	
	public String viewEmployee(){
		ActionContext context = ActionContext.getContext();
		EmployeeDTO employeeDTO = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(SystemContant.workDateType);
		Date startTime =null;
		Date endTime = null;
		if(StringUtil.isNotBlank(endDate)){
			try {
				endTime = dateFormat.parse(endDate);
			} catch (ParseException e) {
			}
		}
		if(endTime ==null){
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			endTime = calendar.getTime();
		}
		if(StringUtil.isNotBlank(startDate)){
			try {
				startTime = dateFormat.parse(startDate);
			} catch (ParseException e) {
			}
		}
		if(startTime !=null && endTime != null && startTime.after(endTime)){
			context.put("error", "开始时间大于结束时间");
			return SUCCESS;
		}
		if(name!=null && !"".equals(name.trim())){
			employeeDTO = employeeDtoUtil.getExtraEmployeeDTO(employeeManager.findUniqueByName(name),startTime,endTime);
		}else{
			employeeDTO = employeeDtoUtil.getExtraEmployeeDTO(employeeManager.findUniqueById(id),startTime,endTime);
		}
		
		context.put("employeeDTO", employeeDTO);
		return SUCCESS;
	}
	
	public String listEmployee(){
		ActionContext context = ActionContext.getContext();
		EmployeeQuery query = new EmployeeQuery();
		if(StringUtil.isNotBlank(name)){
			query.setName(name);
		}
		query.setP(p);
		query.setPageSize(pageSize);
		List<Employee> employees = employeeManager.queryEmployees(query);
		List<EmployeeDTO> employeeDTOs = employeeDtoUtil.getEmployeeDTOList(employees);
		context.put("employeeDTOs", employeeDTOs);
		context.put("query", query);
		context.put("timeTable", getTimeTable(new Date(),false));
		return SUCCESS;
	}

	public String getName() {
		return name;
	}
	
	public List<String> getTimeTable(Date date,boolean add){
		if(date == null){
			date = new Date();
		}
		List<String> timeTable = new ArrayList<String>();
		SimpleDateFormat dateFormat = new SimpleDateFormat(SystemContant.workDateType);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if(add){
			for (int i = 0; i < 7; i++) {
				date = calendar.getTime();
				String time = dateFormat.format(date);
				timeTable.add(time);
				calendar.add(Calendar.DATE, 1);
			}
		}else{	
			for (int i = 0; i < 7; i++) {
				date = calendar.getTime();
				String time = dateFormat.format(date);
				timeTable.add(time);
				calendar.add(Calendar.DATE, -1);
			}
			Collections.reverse(timeTable);
		}		
		return timeTable;
	}
	
	public String changeTime(){
		SimpleDateFormat dateFormat = new SimpleDateFormat(SystemContant.workDateType);
		json.put("status", false);
		try {
			Date date = dateFormat.parse(time);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, add?1:-1);
			List<String> timeTable = getTimeTable(calendar.getTime(), add);
			JSONArray jsonArray = JSONArray.fromObject(timeTable);
			json.put("status", true);
			json.put("timeTable", jsonArray);			
		} catch (ParseException e) {
		}
		return SUCCESS;
	}
	
	public String employeeDownload(){
		ActionContext context = ActionContext.getContext();
		SimpleDateFormat dateFormat = new SimpleDateFormat(SystemContant.workDateType);
		Date startTime =null;
		Date endTime = null;
		if(StringUtil.isNotBlank(endDate)){
			try {
				endTime = dateFormat.parse(endDate);
			} catch (ParseException e) {
			}
		}
		if(endTime ==null){
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			endTime = calendar.getTime();
		}
		if(StringUtil.isNotBlank(startDate)){
			try {
				startTime = dateFormat.parse(startDate);
			} catch (ParseException e) {
			}
		}
		if(startTime !=null && endTime != null &&startTime.after(endTime)){
			context.put("error", "开始时间大于结束时间");
			return ERROR;
		}
		ByteArrayOutputStream os = null;
		try {
			if(name!=null && !"".equals(name.trim())){
				Employee employee = employeeManager.findUniqueByName(name);
				os = employeeDtoUtil.getEmployeeExcel(employee,startTime,endTime);
				fileName = employee.getName();
			}else{
				Employee employee = employeeManager.findUniqueById(id);
				os = employeeDtoUtil.getEmployeeExcel(employee,startTime,endTime);
				fileName = employee.getName();
			}
			this.inputStream = new ByteArrayInputStream(os.toByteArray());
		} catch (Exception e) {
			return ERROR;
		} finally {
			if(os !=null){
				try {
					os.flush();
					os.close();
				} catch (IOException e) {
				}
			}			
		}		
		return SUCCESS;
	}
	
	public InputStream getTargetFile() throws Exception{
		return this.inputStream;
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

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

	public String getDownLoadfileName() {
		try {
			return new String(fileName.getBytes(),"ISO8859-1")+".xls";
		} catch (UnsupportedEncodingException e) {
			return "excel.xls";
		}
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	

}
