package com.javabatchmanager.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

public interface JobService {

	public JobExecution start(String jobName, String jobParams) 
			throws NoSuchJobException, 
			JobExecutionAlreadyRunningException, 
			JobRestartException, 
			JobInstanceAlreadyCompleteException, 
			JobParametersInvalidException;
	
	public void stop (Long executionId);
	
	/**
	 * These jobs can be launched.
	 * @return
	 */
	public Collection<String> getAllLaunchableJobNameList();
	
	/**
	 * These jobs are saved in db.
	 * @return
	 */
	public List<String> getAllJobNamesFromRepo();
	
	public Job getJobByName(String jobName) throws NoSuchJobException;
	
	public JobInstance getJobInstanceById(Long instanceId);
	
	public List<JobExecution> getAllJobExecutionsList();
	
	public List<JobExecution> getJobExecutions(JobInstance jobInstance);
	
	public List<JobInstance> getJobInstances(String jobName, int start, int count);
	
	public JobExecution getJobExecutionById(Long id);
	

	
	
}
