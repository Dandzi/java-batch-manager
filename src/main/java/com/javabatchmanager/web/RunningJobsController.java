package com.javabatchmanager.web;

import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobExecutionListDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.service.JobService;

@Controller
@RequestMapping("/running-jobs")
public class RunningJobsController extends AbstractController{
	private final static Logger logger = Logger.getLogger(RunningJobsController.class.getName());

	@RequestMapping(method = RequestMethod.GET)
	public String viewRunningExecutions(ModelMap model) {
		JobExecutionListDto execList = new JobExecutionListDto();

		try {
			execList.setAllJobExecutions(jobServiceJSR.getAllRunningJobExecutions());
			execList.getAllJobExecutions().addAll(jobServiceSpring.getAllRunningJobExecutions());
		} catch (BaseBatchException e) {
			e.printStackTrace();
		}
		model.addAttribute("jobExecutionsList",execList);

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
		List<JobExecutionDto> toRemove = new ArrayList<>();
		for (JobExecutionDto jobExec : jobExecList.getAllJobExecutions()) {
			if(jobExec.getIdAndType() != null){
				try {
					logger.info("Trying to stop job"+jobExec.getJobName());
						String[] parsedJobExec = jobExec.getIdAndType().split("_");
						JobService jobService = getJobService(parsedJobExec[1]);
						Long id = Long.parseLong(parsedJobExec[0]);
						jobService.stop(id);
						toRemove.add(jobExec);
						i++;
					} catch (BaseBatchException e) {
						e.printStackTrace();
						errors.rejectValue("allJobExecutions["+i+"]",
								"job.running.error.not.exist", new Object[] {jobExec.getJobName(),jobExec.getParameters()},null);
						jobExec.setStop(false);
					}			
			}
		}	
		logger.info("Job successfully stopped");
		return "redirect:/running-jobs";
	}


}
