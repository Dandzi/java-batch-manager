package com.javabatchmanager.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.service.JobService;
import com.javabatchmanager.utils.SpringDtoCreatorUtils;
import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.error.BaseBatchException;

@Controller
@RequestMapping("/past-jobs")
public class PastJobsController extends AbstractController{

	
	private List<String> pastJobsExecutionsList = new ArrayList<String>();
	
	//private List<JobInstanceDto> jobInstanceDtos = new ArrayList<JobInstanceDto>();
	
	//@ModelAttribute("executedjoblist")
	/*public Collection<String> getExecutedJobList(){
		pastJobsExecutionsList = jobService.getAllLaunchableJobNameList();
		return pastJobsExecutionsList;
	}*/
	
	/*@ModelAttribute("jobInstanceDto")
	public List<JobInstanceDto> getJobInstanceDtos(){
		return jobService.getJobInstanceDtos();
	}*/
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String viewPastJobs(ModelMap model){
		Set<String> jobNames=null;
		try {
			jobNames = jobService.getJobNames();
		} catch (BaseBatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jobNames.add("fake");
		model.addAttribute("jobNames", jobNames);
		
		//model.addAttribute("jobInstance", viewPastInstances(jobNames.iterator().next(), 0,10));
		//model.addAttribute("jobExecutions", viewPastExecutions(jobNames.iterator().next()));
		return "past-jobs";
	}

	/*@RequestMapping(value = "{jobName}", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> viewPastJobsNames(@PathVariable String jobName, ModelMap model){
		Set<String> jobNames = jobService.getUniqueJobNames();
		jobNames.add("test");
		jobNames.add("skuska");
		jobNames.add("inaskuska");
		model.addAttribute("jobNames", jobNames);
		model.addAttribute("jobInstances", viewPastInstances(jobName, 0,10));
		model.addAttribute("jobExecutions", viewPastExecutions(jobNames.iterator().next()));
		return jobNames;
	}*/
	
	@RequestMapping(value = "{jobName}", method = RequestMethod.GET)
	@ResponseBody
	public List<JobInstanceDto> viewPastInstances(@PathVariable String jobName, @RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "10") int count){
		List<JobInstanceDto> jobInstances = new ArrayList<JobInstanceDto>();
		try {
			jobInstances = jobService.getJobInstances(jobName, start, count);
		} catch (BaseBatchException e) {
			return jobInstances;
		}
		return jobInstances;
	}
	
	
	/*
	 * TODO make this method better
	 */
	@RequestMapping(value = "{jobName}/{jobInstanceId}", method = RequestMethod.GET)
	@ResponseBody
	public List<JobExecutionDto> viewPastExecutions(@PathVariable long jobInstanceId, @PathVariable String jobName) throws BaseBatchException{
		JobInstanceDto jobInstDto = SpringDtoCreatorUtils.createJobInstanceDtoFromSpring(jobName, null, jobInstanceId);
		List<JobExecutionDto> jobExecDto=jobService.getJobExecutions(jobInstDto);
		//List<JobExecutionDto> jobExecDto=new ArrayList<JobExecutionDto>();
		/*for(JobExecutionDto jobExec: jobExecs){
			jobExecDto.add(DtoCreatorUtils.createJobExecutionDto(jobExec));
		}*/
		return jobExecDto;
	}
	


}
