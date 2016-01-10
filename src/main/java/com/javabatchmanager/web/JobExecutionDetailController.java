package com.javabatchmanager.web;

import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.service.JobService;

@Controller
@RequestMapping("job-execution")
public class JobExecutionDetailController {

	@Autowired
	private JobService jobService;

	@RequestMapping(method = RequestMethod.GET)
	public String getJobExecution(){
		return "job-execution";
	}
	
	@RequestMapping(value="{id}",method=RequestMethod.GET)
	public String getJobExecutionById(@PathVariable long id, ModelMap model){
		JobExecutionDto jobExec;
		try {
			jobExec = jobService.getJobExecutionById(id);
			model.addAttribute("jobExecution", jobExec);
		} catch (BaseBatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "job-execution";
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.POST)
	public String restart(@PathVariable long id, ModelMap model){
		try {
			JobExecutionDto jobExec = jobService.restart(id);
			model.addAttribute("jobExecution", jobExec);
		} catch (BaseBatchException e) {

		}
		
		return "job-execution";
	}
	
	
	public JobService getJobService() {
		return jobService;
	}

	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}
	
	
}
