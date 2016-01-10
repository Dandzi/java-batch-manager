package com.javabatchmanager.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobExecutionListDto;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.service.JobService;
import com.javabatchmanager.watchers.Monitor;

@Controller
@RequestMapping("/running-jobs")
public class RunningJobsController extends AbstractController{
	private final static Logger logger = Logger.getLogger(RunningJobsController.class.getName());

	@ModelAttribute("jobExecutionsList")
	public JobExecutionListDto getJobExecutions(ModelMap model) {
		JobExecutionListDto jeld = new JobExecutionListDto();
		try {
			jeld.setAllJobExecutions(jobService.getAllRunningJobExecutions());
		} catch (BaseBatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("jobExecutionsList", jeld);
		return jeld;
	}

	/*
	 * TODO 2 execution with same name may cause problem in jsp-same atribute
	 * name
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String viewRunningExecutions(ModelMap model) {
		return "running-jobs";
	}
	
	@RequestMapping(value = "/stop", method = RequestMethod.POST)
	public String stop(@ModelAttribute("jobExecutionsList") JobExecutionListDto jobExecList,
			BindingResult errors, ModelMap model) {
		if(jobExecList.getAllJobExecutions().isEmpty()){
			errors.reject("job.running.error.not.selected");
			return "running-jobs";
		}
		int i=0;
		for (JobExecutionDto jobExec : jobExecList.getAllJobExecutions()) {
			if (jobExec.isStop()) {
				try {
					logger.info("Trying to stop job"+jobExec.getJobName());
					jobService.stop(jobExec.getJobExecutionId());
					i++;
				} catch (Exception e) {
					errors.rejectValue("allJobExecutions["+i+"]",
							"job.running.error.not.exist", new Object[] {jobExec.getJobName(),jobExec.getParameters()},null);
					jobExec.setStop(false);
				}
			}
		}
		if (errors.hasErrors()) {
			return "running-jobs";
		}
		logger.info("Job successfully stopped");
		return "redirect:/running-jobs";
	}


}
