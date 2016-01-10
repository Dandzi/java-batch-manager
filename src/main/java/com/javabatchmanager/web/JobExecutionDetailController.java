package com.javabatchmanager.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.service.JobService;

@Controller
@RequestMapping("job-execution/")
public class JobExecutionDetailController extends AbstractController{


	@RequestMapping(method = RequestMethod.GET)
	public String getJobExecution(){
		return "job-execution";
	}
	
	@RequestMapping(value="{idandtype}",method=RequestMethod.GET)
	public String getJobExecutionById(@PathVariable String idandtype, ModelMap model){
		JobExecutionDto jobExec;
		try {
			String[] parsedJobName = idandtype.split("_");
			JobService jobService = getJobService(parsedJobName[1]);
			Long execid = Long.parseLong(parsedJobName[0]);
			jobExec = jobService.getJobExecutionById(execid);
			model.addAttribute("jobExecution", jobExec);
		} catch (BaseBatchException e) {
			e.printStackTrace();
		}
		
		return "job-execution";
	}
	
	@RequestMapping(value="{idandtype}", method=RequestMethod.POST)
	public String restart(@PathVariable String idandtype, ModelMap model, @ModelAttribute("jobExecution") JobExecutionDto jobExecDto, BindingResult errors){
		try {
			String[] parsedJobName = idandtype.split("_");
			JobService jobService = getJobService(parsedJobName[1]);
			Long execid = Long.parseLong(parsedJobName[0]);		
			jobExecDto = jobService.restart(execid);
			model.addAttribute("jobExecution", jobExecDto);
		} catch (BaseBatchException e) {
			e.printStackTrace();
			ControllerExceptionHandler.handleException(e, errors, new Object[] {jobExecDto.getJobName(), jobExecDto.getParameters() });
		}
		if(errors.hasErrors()){
			return getJobExecutionById(idandtype, model);
		}
		
		return "redirect:/job-execution/"+idandtype;
	}
	
	
	
	
}
