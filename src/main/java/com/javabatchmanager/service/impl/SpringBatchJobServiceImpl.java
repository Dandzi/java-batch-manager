
package com.javabatchmanager.service.impl;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.OptimisticLockingFailureException;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.error.ExceptionCause;
import com.javabatchmanager.service.JobService;
import com.javabatchmanager.utils.SpringDtoCreatorUtils;
import com.javabatchmanager.watchers.SpringBatchJobListener;
import com.javabatchmanager.websocket.JobNotificationEndpoint;


/**
 * Core implementation of JobService interface, this implmentations focus on Spring Batch jobs. 
 * @author daniel
 *
 */
@Qualifier("SpringBatch")
public class SpringBatchJobServiceImpl implements JobService{
	
	
	private JobRegistry jobRegistry;
	
	private JobExplorer jobExplorer;

	private JobOperator jobOperator;
	
	private Map<String,SpringBatchJobListener> registeredLsteners = new HashMap<String,SpringBatchJobListener>();




	/*
	 * start, stop, restart
	 */
	
	public JobExecutionDto start(String jobName, String jobParams) throws BaseBatchException {			
		AbstractJob job=null;
		try {
			job = (AbstractJob) jobRegistry.getJob(jobName);
		} catch (NoSuchJobException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.NO_SUCH_JOB);
		}
		Long jobExecutionId = null;
		if(job != null)
		{
			try {
				if(!registeredLsteners.containsKey(jobName)){
					SpringBatchJobListener listener = new SpringBatchJobListener();
					job.registerJobExecutionListener(listener);
					registeredLsteners.put(jobName, listener);
				}
				jobExecutionId = jobOperator.start(jobName, jobParams);				
			} catch (JobParametersInvalidException e) {
				throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_PARAMETERS_INVALID);
			} catch (NoSuchJobException e) {
				throw new BaseBatchException(e.getMessage(), e, ExceptionCause.NO_SUCH_JOB);
			} catch (JobInstanceAlreadyExistsException e) {
				throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_INSTANCE_ALREADY_EXISTS);
			} catch (UnexpectedJobExecutionException e) {
				e.printStackTrace();
			}
		}				
		return getJobExecutionById(jobExecutionId);
	}
	
	//toto operator is null
	
	@Override
	public JobExecutionDto stop(Long executionId) throws BaseBatchException {
		JobExecutionDto jobExecDto = getJobExecutionById(executionId);
		try {
			jobOperator.stop(jobExecDto.getJobExecutionId());
			jobExecDto.setStatus("STOPPED");
		} catch (NoSuchJobExecutionException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.NO_SUCH_JOB);
		} catch (JobExecutionNotRunningException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_EXECUTION_NOT_RUNNING);
		} 
		
		return jobExecDto;
	}

	@Override
	public JobExecutionDto restart(Long executionId) throws BaseBatchException {
		Long jobExecID=null;
		JobExecution jobExec = jobExplorer.getJobExecution(executionId);
		if (jobExec == null) {
			throw new BaseBatchException("JobExecution doesn't exists.", ExceptionCause.NO_SUCH_JOB_EXECUTION);
		}
		try {
			if(jobExec.getStatus()==BatchStatus.STOPPED || jobExec.getStatus()==BatchStatus.FAILED){
				jobExecID = jobOperator.restart(jobExec.getId());
			}
		} catch (JobInstanceAlreadyCompleteException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_INSTANCE_ALREADY_COMPLETE);
		} catch (NoSuchJobExecutionException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.NO_SUCH_JOB_EXECUTION);
		} catch (NoSuchJobException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.NO_SUCH_JOB);
		} catch (JobRestartException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_RESTART);
		} catch (JobParametersInvalidException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_PARAMETERS_INVALID);
		} 
		if(jobExecID!=null){
			return getJobExecutionById(jobExecID);
		}
		return getJobExecutionById(executionId);
	}
	
	
	
	//----------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------	
	/*
	 * Get Dtos executions and instances methods
	 */
	//----------------------------------------------------------------------------------------------------------------------------		
	//----------------------------------------------------------------------------------------------------------------------------		
	
	@Override
	public Set<String> getJobNames() {		
		return jobOperator.getJobNames();
	}
	
	
	@Override
	public List<JobExecutionDto> getJobExecutions(JobInstanceDto jobInstanceDto) throws BaseBatchException {
		List<Long> jobExecutions=null;
		if(jobInstanceDto == null){
			return new ArrayList<JobExecutionDto>();
		}
		JobInstance jobInstance = getOriginalJobInstance(jobInstanceDto.getJobInstanceId());
		if(jobInstance == null){
			return new ArrayList<JobExecutionDto>();
		}
		try {
			jobExecutions = jobOperator.getExecutions(jobInstance.getInstanceId());
		} catch (NoSuchJobInstanceException e) {
			// should never be thrown, we get jobInstance from repo
			e.printStackTrace();
		}
		List<JobExecutionDto> jobExecsDtos = new ArrayList<JobExecutionDto>();
		for(Long jobExec: jobExecutions){
			JobExecutionDto jobexecdto = getJobExecutionById(jobExec);
			jobExecsDtos.add(jobexecdto);
		}
		return jobExecsDtos;
	}


	@Override
	public List<JobInstanceDto> getJobInstances(String jobName, int start,	int count) throws BaseBatchException {
		try {
			List<JobInstanceDto> instancesDto = new ArrayList<>();
			if(start<0 || count <0){
				return instancesDto;
			}
			List<Long> jobInstances = jobOperator.getJobInstances(jobName, start, count);
			for(Long instanceId: jobInstances){
				JobInstance jobInst = getOriginalJobInstance(instanceId);
				JobInstanceDto instanceDto=SpringDtoCreatorUtils.createJobInstanceDtoFromSpring(jobInst);
				instancesDto.add(instanceDto);
			}
			return instancesDto;
		} catch (NoSuchJobException e) {
			return new ArrayList<JobInstanceDto>();
		}		
	}

	@Override
	public Set<JobExecutionDto> getRunningJobExecutions(String jobName) throws BaseBatchException {
		if(jobName==null){
			return new HashSet<JobExecutionDto>();
		}
		try {
			Set<Long> execIds=jobOperator.getRunningExecutions(jobName);
			Set<JobExecutionDto> execDtos= new HashSet<>();
			for(Long id: execIds){
				execDtos.add(getJobExecutionById(id));
			}
			return execDtos;
		} catch (NoSuchJobException e) {
			return new HashSet<JobExecutionDto>();
		}


	}
	
	@Override
	public List<JobExecutionDto> getAllRunningJobExecutions() throws BaseBatchException {
		Set<String> jobNames = getJobNames();
		List<JobExecutionDto> jobExecutionsDto = new ArrayList<JobExecutionDto>();
		for(String jobName:jobNames){
			jobExecutionsDto.addAll(getRunningJobExecutions(jobName));
		}
		return jobExecutionsDto;
	}

	


	@Override
	public JobExecutionDto getJobExecutionById(Long executionId) throws BaseBatchException {
		JobExecution jobExec=jobExplorer.getJobExecution(executionId);
		if(jobExec == null){
			throw new BaseBatchException("Execution with id " + executionId +"does not exists", ExceptionCause.NO_SUCH_JOB_EXECUTION);
		}
		String jobName = jobExec.getJobInstance().getJobName();
		boolean isRestartable = false;
		try {
			Job job = jobRegistry.getJob(jobName);
			isRestartable = job.isRestartable();
		} catch (NoSuchJobException e) {
			//should never be thrown
			e.printStackTrace();
		}		
		JobExecutionDto jobExecDto=SpringDtoCreatorUtils.createJobExecDtoFromSpring(jobExec);
		String code = jobExec.getStatus().name();
		if(BatchStatus.STOPPED.name().equals(code) || BatchStatus.FAILED.name().equals(code)){
			jobExecDto.setRestartable(isRestartable);
		}
		return jobExecDto;
	}
	

	
	//----------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------
	/*
	 * Getters for original jobExecution an jobInstance 
	 */
	//----------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------
	public List<JobExecution> getOriginalJobExecution(JobInstance jobInstance){
		List<JobExecution>jobExecs=jobExplorer.getJobExecutions(jobInstance);
		return jobExecs;
	}
	
	public JobInstance getOriginalJobInstance(Long instanceId){
		JobInstance jobInst = jobExplorer.getJobInstance(instanceId);
		return jobInst;
	}
	
	
	

	//----------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------		
	/*
	 * Getters and Setters 
	 */	
	//----------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------	
	
	public JobExplorer getJobExplorer() {
		return jobExplorer;
	}

	public void setJobExplorer(JobExplorer jobExplorer) {
		this.jobExplorer = jobExplorer;
	}
	
	JobRegistry getJobRegistry() {
		return jobRegistry;
	}

	public void setJobRegistry(JobRegistry jobRegistry) {
		this.jobRegistry = jobRegistry;
	}


	public JobOperator getJobOperator() {
		return jobOperator;
	}

	public void setJobOperator(JobOperator jobOperator) {
		this.jobOperator = jobOperator;
	}

}
