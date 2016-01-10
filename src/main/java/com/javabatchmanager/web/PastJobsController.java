package com.javabatchmanager.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.utils.SpringDtoCreatorUtils;
import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.service.JobService;

@Controller
@RequestMapping("/past-jobs")
public class PastJobsController extends AbstractController{	
	
	@RequestMapping(method = RequestMethod.GET)
	public String viewPastJobs(ModelMap model){
		Set<String> jsrjobs;
		Set<String> springjobs;
		jsrjobs = jobServiceJSR.getJobNames();		
		
		springjobs = jobServiceSpring.getJobNames();
		model.addAttribute("jsrjobs", jsrjobs);
		model.addAttribute("springjobs",springjobs);
		if(springjobs.isEmpty()&&jsrjobs.isEmpty()){ 
			return "no-job-available"; 
		}
		return "past-jobs";
	}
	
	@RequestMapping(value = "{jobName}", method = RequestMethod.GET)
	@ResponseBody
	public List<JobInstanceDto> viewPastInstances(@PathVariable String jobName, 
			@RequestParam(defaultValue = "0") int start, 
			@RequestParam(defaultValue = "10") int count){
		List<JobInstanceDto> jobInstances = new ArrayList<JobInstanceDto>();
		try {
			String[] parsedJobName = jobName.split("_");
			JobService jobService = getJobService(parsedJobName[1]);
			jobInstances = jobService.getJobInstances(parsedJobName[0], start, count);
		} catch (BaseBatchException e) {
			e.printStackTrace();
		}
		return jobInstances;
	}
	
	
	@RequestMapping(value = "{jobName}/{idandtype}", method = RequestMethod.GET)
	@ResponseBody
	public List<JobExecutionDto> viewPastExecutions(@PathVariable String idandtype, @PathVariable String jobName) throws BaseBatchException{
		String[] parsedJobName = idandtype.split("_");
		JobService jobService = getJobService(parsedJobName[1]);
		Long execid = Long.parseLong(parsedJobName[0]);
		JobInstanceDto jobInstDto = SpringDtoCreatorUtils.createJobInstanceDtoFromSpring(parsedJobName[0], null, execid);
		List<JobExecutionDto> jobExecDto=jobService.getJobExecutions(jobInstDto);
		return jobExecDto;
	}
	


}
