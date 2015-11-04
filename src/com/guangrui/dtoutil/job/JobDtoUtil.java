package com.guangrui.dtoutil.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.guangrui.dto.job.JobDTO;
import com.guangrui.model.job.Job;
import com.guangrui.util.SystemContant;

@Component
public class JobDtoUtil {
	
	public JobDTO getJobDTO(Job job){
		if(job == null){
			return null;
		}
		JobDTO jobDTO = new JobDTO();
		SimpleDateFormat dateFormat = new SimpleDateFormat(SystemContant.workDateType);
		jobDTO.setDate(dateFormat.format(job.getWorkDate()));
		jobDTO.setEmployeeName(job.getEmployee().getName());
		jobDTO.setProjectName(job.getProject().getName());
		jobDTO.setWorkTime(job.getWorkTime());
		jobDTO.setOverTime(job.getOverTime());
		jobDTO.setWeekendTime(job.getWeekendTime());
		jobDTO.setId(job.getId());
		return jobDTO;
	}
	
	public List<JobDTO> getJobDTOList(List<Job> jobs){
		if(jobs ==null || jobs.isEmpty()){
			return null;
		}
		List<JobDTO> jobDTOs = new ArrayList<JobDTO>();
		for (Job job : jobs) {
			JobDTO jobDTO = this.getJobDTO(job);
			jobDTOs.add(jobDTO);
		}
		return jobDTOs;
	}

}
