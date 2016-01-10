package com.javabatchmanager.service;

import java.util.Collection;
import java.util.List;
import java.util.Observer;
import java.util.Set;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import com.google.common.util.concurrent.ListenableFuture;
import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.error.ExceptionCause;
import com.javabatchmanager.watchers.JobExecutionObserver;
import com.javabatchmanager.watchers.Listener;

public interface JobService {

	
	//Starts job with name jobName and parameters jobParams.
	/**
	 * 
	 * @param jobName
	 * @param jobParams
	 * @param jobExecutionListener
	 * @return
	 * @throws BaseBatchException 
	 */
	public JobExecutionDto start(String jobName, String jobParams) throws BaseBatchException;

	 //Restart job execution with id executionId.
	/**
	 * 
	 * Possible exception causes:
	 * <br/>JOB_INSTANCE_ALREADY_COMPLETE
	 * <br/>NO_SUCH_JOB_EXECUTION
	 * <br/>NO_SUCH_JOB
	 * <br/>JOB_RESTART
	 * <br/>JOB_PARAMETERS_INVALID
	 * @param executionId
	 * @return
	 * @throws BaseBatchException 
	 */
	public JobExecutionDto restart(Long executionId) throws BaseBatchException;

	/**
	 * Stop running executions.
	 * 
	 * @param executionId
	 * @throws BaseBatchException 
	 * 
	 */
	public JobExecutionDto stop(Long executionId) throws BaseBatchException;

	//public void abandon(Long executionId);

	/**
	 * These jobs can be launched.
	 * 
	 * @return
	 * @throws BaseBatchException 
	 */
	public Set<String> getJobNames() throws BaseBatchException;

	/**
	 * These jobs are saved in db.
	 * 
	 * @return
	 * @throws BaseBatchException 
	 */
	//public List<String> getAllJobNamesFromRepo() throws BaseBatchException;

	/**
	 * Find job with name jobName at Runtime.
	 * 
	 * @param jobName
	 * @return
	 * 
	 */
	//public Job getJobByName(String jobName);

	/**
	 * !!!! mention that jsr 352 dont have method for getting jobinstance by id 
	 * @return
	 */
	
	public JobInstanceDto getJobInstanceById(Long instanceId);

	//public List<JobExecutionDto> getAllJobExecutionsList();

	public List<JobExecutionDto> getJobExecutions(JobInstanceDto jobInstance) throws BaseBatchException;

	public List<JobExecutionDto> getJobExecutions(Long jobInstanceId) throws BaseBatchException;

	public List<JobInstanceDto> getJobInstances(String jobName, int start, int count) throws BaseBatchException;

	public JobExecutionDto getJobExecutionById(Long id) throws BaseBatchException;

	/**
	 * Returns set of running job executions of given job name.
	 * 
	 * @param jobName
	 * @return
	 * @throws BaseBatchException 
	 */

	public Set<JobExecutionDto> getRunningJobExecutions(String jobName) throws BaseBatchException;

	/**
	 * Finds all running job executions.
	 * 
	 * @return
	 * @throws BaseBatchException 
	 */
	public List<JobExecutionDto> getAllRunningJobExecutions() throws BaseBatchException;

	public List<JobParameters> getJobParametersOfJobInstance(JobInstance jobInstance);

	//public List<JobInstanceDto> getJobInstanceDtos();

	//public Set<String> getUniqueJobNames();

	
}
