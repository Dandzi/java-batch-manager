package com.javabatchmanager.web;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javabatchmanager.service.JobService;
import com.javabatchmanager.web.GlobalJobVar.JobType;

public abstract class AbstractController {
	
	static JobType activeJobService;
	
	@Autowired
	@Qualifier("JSR352Batch")
	protected JobService jobServiceJSR;
	
	@Autowired
	@Qualifier("SpringBatch")
	protected JobService jobServiceSpring;	
	
	protected static JobService jobService;
	
	@PostConstruct
	private void setJobService(){
		jobService = jobServiceJSR;
		activeJobService = JobType.JSR_352_BATCH;
	}
	
	@ModelAttribute("jobTypeList")
	public JobType[] getJobType(ModelMap model){
		model.addAttribute("activeJobService", activeJobService);
		model.addAttribute("globVar", new GlobalJobVar());
		return JobType.values();
	}
	public static JobService getJobService() {
		return jobService;
	}

	public static void setJobService(JobService jobService) {
		AbstractController.jobService = jobService;
	}
	
	
}
