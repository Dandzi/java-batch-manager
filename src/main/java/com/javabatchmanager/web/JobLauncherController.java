package com.javabatchmanager.web;

import java.text.DateFormat;
import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.scheduling.JobTaskScheduler;
import com.javabatchmanager.service.JobService;

@Controller
@RequestMapping("/launchable-jobs")
public class JobLauncherController extends AbstractController{

	private final static Logger logger = Logger.getLogger(JobLauncherController.class.getName());
	

	@Autowired
	@Qualifier("JobTaskScheduler")
	private JobTaskScheduler jobTaskScheduler;
	
	@ModelAttribute("job")
	public JobInstanceDto getJobList(HttpServletRequest request, ModelMap model) {
		JobInstanceDto jid = new JobInstanceDto();
		return jid;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String viewJobList(ModelMap model) {
		Set<String> jsrjobs;
		Set<String> springjobs;
		jsrjobs = jobServiceJSR.getJobNames();
		springjobs = jobServiceSpring.getJobNames();
		model.addAttribute("jsrjobs", jsrjobs);
		model.addAttribute("springjobs",springjobs);
		model.addAttribute("futurejobs",jobTaskScheduler.getFutureJobs());
		if(springjobs.isEmpty()&&jsrjobs.isEmpty()){ 
			return "no-job-available"; 
		}
		 
		return "launchable-jobs";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String launch(@ModelAttribute("job") JobInstanceDto jid,
			BindingResult errors, ModelMap model) {

		if (jid==null || jid.getJobName() == null) {
			errors.rejectValue("jobName", "job.start.error.name.null");
			return viewJobList(model);
		}

		try {
			if(jid.getStartTime() != null){
				jobTaskScheduler.launch(jid, jid.getStartTime());
			}else{
				logger.info("Starting job" + jid.getJobName());
				String[] parsedJobName = jid.getJobName().split("_");
				JobService jobService = getJobService(parsedJobName[1]);
				JobExecutionDto jobExecution = jobService.start(parsedJobName[0], jid.getParameters());
			}
		} catch (BaseBatchException e) {
			e.printStackTrace();
			ControllerExceptionHandler.handleException(e, errors, new Object[] {jid.getJobName(), jid.getParameters() });
		}

		if (errors.hasErrors()) {
			return viewJobList(model);
		}
		logger.info("Job " + jid.getJobName() + " successfully started.");
		return "redirect:/launchable-jobs";
	}

	public JobTaskScheduler getJobTaskScheduler() {
		return jobTaskScheduler;
	}

	public void setJobTaskScheduler(JobTaskScheduler jobTaskScheduler) {
		this.jobTaskScheduler = jobTaskScheduler;
	}

	


}
