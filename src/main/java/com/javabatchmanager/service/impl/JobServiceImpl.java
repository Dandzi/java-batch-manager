package com.javabatchmanager.service.impl;



import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.AutomaticJobRegistrar;
import org.springframework.batch.core.configuration.support.JobLoader;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.support.PropertiesConverter;
import org.springframework.stereotype.Service;

import com.javabatchmanager.service.JobService;

@Service
public class JobServiceImpl implements JobService{
	
	private JobLoader jobLoader;
	
	private JobLauncher jobLauncher;
	
	private JobRepository jobRepository;
	
	private JobRegistry jobRegistry;
	
	private AutomaticJobRegistrar automaticJobRegistrar;
	
	private JobExplorer jobExplorer;

	//private JobOperator jobOperator;
	
	private JobParametersConverter jobParametersConverter;
	
	
	public JobExecution start(String jobName, String jobParams) 
			throws NoSuchJobException, 
			JobExecutionAlreadyRunningException, 
			JobRestartException, 
			JobInstanceAlreadyCompleteException, 
			JobParametersInvalidException{
		
		JobExecution jobExecution=null;
		Job job = null;
		if(jobName==null){
			throw new NullPointerException("JobName is null");
		}
		job = jobRegistry.getJob(jobName);
		JobParameters jobParametes = jobParametersConverter
										.getJobParameters(PropertiesConverter.stringToProperties(jobParams));
		
		if(job != null)
		{
			jobExecution = jobLauncher.run(job, jobParametes);
		}
		
		return jobExecution;
	}
	
	@Override
	public Collection<String> getAllLaunchableJobNameList() {
		Collection<String> jobList = jobRegistry.getJobNames(); 
		
		return jobList;
	}
	
	public JobLoader getJobLoader() {
		return jobLoader;
	}

	public void setJobLoader(JobLoader jobLoader) {
		this.jobLoader = jobLoader;
	}

	public JobLauncher getJobLauncher() {
		return jobLauncher;
	}

	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	public JobRepository getJobRepository() {
		return jobRepository;
	}

	public void setJobRepository(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	public JobRegistry getJobRegistry() {
		return jobRegistry;
	}

	public void setJobRegistry(JobRegistry jobRegistry) {
		this.jobRegistry = jobRegistry;
	}

	public AutomaticJobRegistrar getAutomaticJobRegistrar() {
		return automaticJobRegistrar;
	}

	public void setAutomaticJobRegistrar(AutomaticJobRegistrar automaticJobRegistrar) {
		this.automaticJobRegistrar = automaticJobRegistrar;
	}

	public JobParametersConverter getJobParametersConverter() {
		return jobParametersConverter;
	}


	public void setJobParametersConverter(
			JobParametersConverter jobParametersConverter) {
		this.jobParametersConverter = jobParametersConverter;
	}

	@Override
	public void stop(Long executionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Job getJobByName(String jobName) throws NoSuchJobException {
		Job job = jobRegistry.getJob(jobName);
		if(job == null){
			throw new NoSuchJobException("Job with name" + jobName + " does not exist");
		}		
		return job;
	}

	@Override
	public JobInstance getJobInstanceById(Long instanceId) {
		JobInstance jobInstance = jobExplorer.getJobInstance(instanceId);
		return jobInstance;
	}


	@Override
	public List<String> getAllJobNamesFromRepo() {
		List<String> jobNames = jobExplorer.getJobNames();
		return jobNames;
	}

	@Override
	public List<JobExecution> getAllJobExecutionsList() {
		List<String> jobNames = getAllJobNamesFromRepo();
		if(jobNames == null){
			throw new NullPointerException("There are no jobs in repository or DataBase");
		}
		List<JobInstance> jobInstances = null;
		for(String jobName: jobNames){
			jobInstances.addAll(getJobInstances(jobName, 5, 5));
		}
		if(jobInstances == null){
			throw new NullPointerException("There are no jobInstances in repository or DataBase");
		}
		
		List<JobExecution> jobExecutions = null;
		for(JobInstance jobInstance: jobInstances){
			jobExecutions.addAll(getJobExecutions(jobInstance));
		}
		return jobExecutions;
	}
	
	@Override
	public List<JobExecution> getJobExecutions(JobInstance jobInstance) {
		List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
		return jobExecutions;
	}

	@Override
	public JobExecution getJobExecutionById(Long id) {
		
		return null;
	}

	@Override
	public List<JobInstance> getJobInstances(String jobName, int start,	int count) {
		List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, start, count);
		return jobInstances;
	}

	public JobExplorer getJobExplorer() {
		return jobExplorer;
	}

	public void setJobExplorer(JobExplorer jobExplorer) {
		this.jobExplorer = jobExplorer;
	}






	
	
	
}
