package com.javabatchmanager.web;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.AutomaticJobRegistrar;
import org.springframework.batch.core.configuration.support.JobLoader;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.service.JobService;

//@Controller
//@RequestMapping("/debug")
public class DebugController{

	/*private JobService jobService;

	private List<JobInstanceDto> jobList = new ArrayList<JobInstanceDto>();
	
	@ModelAttribute("joblist")
	public List<JobInstanceDto> getJobList(ModelMap model){
		if(this.jobList.isEmpty()){
			//Collection<String> jobs = jobService.getAllLaunchableJobNameList();
			for(String jobName: jobs){
				JobInstanceDto jld = new JobInstanceDto();
				jld.setJobName(jobName);
				jld.setParameters("");				
				this.jobList.add(jld);
			}
			JobInstanceDto jld = new JobInstanceDto();
			jld.setParameters("");
			jld.setJobName("tlllllllllllllllllllllest");
			this.jobList.add(jld);
		}
		return jobList;
	}
	
	@ModelAttribute("executedjoblist")
	public Collection<String> getExecutedJobList(){
		List<String> executedJobList = jobService.getAllJobNamesFromRepo();
		return executedJobList;
	}
	
	@RequestMapping(value= "/job-list" , method = RequestMethod.GET)
	public String viewJobList(ModelMap model) throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		for(JobInstanceDto job: jobList){
			model.addAttribute(job.getJobName(), job);
		}
		//model.addAttribute("job", jobList.get(0));
		return "job-list";
    }
		
	@RequestMapping(value = "/job-list/{jobname}", method = RequestMethod.POST)
	public String launch( @ModelAttribute("{jobname}")JobInstanceDto jld, ModelMap model) 
								throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException{
//		JobExecution jobExecution = jobService.start(jld.getJobName(), null);
		
		return "job-list";
	}
	
	@RequestMapping(value="/executed-job-list", method = RequestMethod.GET)
	public String getJobExecutions(ModelMap model){
		return "executed-job-list";
	}
	
	public JobService getJobService() {
		return jobService;
	}

	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}*/
}
