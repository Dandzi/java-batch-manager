package com.javabatchmanager.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.service.JobService;
import com.javabatchmanager.utils.SpringDtoCreatorUtils;
import com.javabatchmanager.watchers.Listener;
import com.javabatchmanager.web.GlobalJobVar.JobType;

@Controller
@RequestMapping("/launchable-jobs")
public class JobLauncherController extends AbstractController{

	private final static Logger logger = Logger.getLogger(JobLauncherController.class.getName());
	
	
	@ModelAttribute("job")
	public JobInstanceDto getJobList(HttpServletRequest request, ModelMap model) {
		JobInstanceDto jid = new JobInstanceDto();
		return jid;
	}

	/*@RequestMapping(value="/setJobService" ,method = RequestMethod.GET)
	public String setJobService(){
		this.jobService = getJobService(); 
	}*/
	
	@RequestMapping(method = RequestMethod.GET)
	public String viewJobList(ModelMap model) {
		Set<String> jobs;
		try {
			jobs = jobService.getJobNames();
			model.addAttribute("jobs", jobs);
		} catch (BaseBatchException e) {
			return "no-job-available";
		}

		/*if(jobs.isEmpty()){ 
			return "no-job-available"; 
		}*/
		 

		return "launchable-jobs";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String launch(@ModelAttribute("job") JobInstanceDto jid,
			BindingResult errors, ModelMap model) {

		if (jid.getJobName() == null) {
			errors.rejectValue("jobName", "job.start.error.name.null");
			return viewJobList(model);
		}
		logger.info("Starting job" + jid.getJobName());
		try {
			JobExecutionDto jobExecution = jobService.start(jid.getJobName(), jid.getParameters());
		} catch (BaseBatchException e) {
			ControllerExceptionHandler.handleException(e, errors, new Object[] {jid.getJobName(), jid.getParameters() });
		}

		if (errors.hasErrors()) {
			return viewJobList(model);
		}
		logger.info("Job " + jid.getJobName() + " successfully started.");
		return "redirect:/launchable-jobs";
	}


}
