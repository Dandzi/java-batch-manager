package com.javabatchmanager.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ServiceLoader;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.AutomaticJobRegistrar;
import org.springframework.batch.core.configuration.support.JobLoader;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.support.PropertiesConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.ListenableFuture;
import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.error.ExceptionCause;
import com.javabatchmanager.service.JobService;
import com.javabatchmanager.utils.SpringDtoCreatorUtils;
import com.javabatchmanager.watchers.JobExecutionListenerImpl;
import com.javabatchmanager.watchers.JobExecutionObserver;
import com.javabatchmanager.watchers.JobExecutionObserverImpl;
import com.javabatchmanager.watchers.Listener;
import com.javabatchmanager.watchers.ObservableJobExecution;
import com.javabatchmanager.watchers.SpringBatchJobListener;
import com.javabatchmanager.websocket.JobNotificationEndpoint;
@Qualifier("SpringBatch")
public class SpringBatchJobServiceImpl implements JobService, ApplicationContextAware{
	
	
	private JobRegistry jobRegistry;
	
	private JobExplorer jobExplorer;

	private JobOperator jobOperator;
	
	private Map<String,SpringBatchJobListener> registeredLsteners = new HashMap<String,SpringBatchJobListener>();
	private static ApplicationContext appContext;



	/*
	 * start, stop, restart, abandon methods 
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
					JobNotificationEndpoint notificationEndpoint = (JobNotificationEndpoint) appContext.getBean("jobNotificationEndpoint");
					SpringBatchJobListener listener = new SpringBatchJobListener(notificationEndpoint);
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
		JobExecutionDto jobExecDto = getJobExecutionById(executionId);
		if(!jobExecDto.getIsRestartable()){
			throw new BaseBatchException("This job execution is not restartable",ExceptionCause.JOB_RESTART);
		}
		try {
			jobOperator.restart(jobExecDto.getJobExecutionId());
			jobExecDto.setStatus("STARTED");
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
		return jobExecDto;
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
	public List<JobExecutionDto> getJobExecutions(JobInstanceDto jobInstance) {
		List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(getOriginalJobInstance(jobInstance.getJobInstanceId()));
		List<JobExecutionDto> jobExecsDtos = new ArrayList<JobExecutionDto>();
		for(JobExecution jobExec: jobExecutions){
			jobExecsDtos.add(SpringDtoCreatorUtils.createJobExecDtoFromSpring(jobExec));
		}
		return jobExecsDtos;
	}


	@Override
	public List<JobInstanceDto> getJobInstances(String jobName, int start,	int count) throws BaseBatchException {
		Set<String> jobNames=getJobNames();
		if(!jobNames.contains(jobNames)){
			throw new BaseBatchException("Job with name" + jobName +"does not exists", ExceptionCause.NO_SUCH_JOB);
		}
		List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, start, count);
		SpringDtoCreatorUtils.createCollectionJobInstDtoFromSpring(jobInstances);
		return (List<JobInstanceDto>) SpringDtoCreatorUtils.createCollectionJobInstDtoFromSpring(jobInstances);
	}

	@Override
	public Set<JobExecutionDto> getRunningJobExecutions(String jobName) {
		Set<JobExecution> jobExec=jobExplorer.findRunningJobExecutions(jobName);
		Set<JobExecutionDto> jobExecs = SpringDtoCreatorUtils.createCollectionJobExecDtoFromSpring(jobExec);
		return jobExecs;
	}
	
	@SuppressWarnings("null")
	@Override
	public List<JobExecutionDto> getAllRunningJobExecutions() {
		Set<String> jobNames = getJobNames();
		List<JobExecutionDto> jobExecutionsDto = new ArrayList<JobExecutionDto>();
		for(String jobName:jobNames){
			jobExecutionsDto.addAll(getRunningJobExecutions(jobName));
		}
		return jobExecutionsDto;
	}

	
	@Override
	public List<JobParameters> getJobParametersOfJobInstance(JobInstance jobInstance) {
		List<JobExecution> jobExecutions=jobExplorer.getJobExecutions(jobInstance);
		List<JobParameters> jobParams=new ArrayList<JobParameters>();
		for(JobExecution jobExecution: jobExecutions){
			jobParams.add(jobExecution.getJobParameters());
		}		
		return jobParams;		
	}

	@Override
	public JobExecutionDto getJobExecutionById(Long executionId) {
		JobExecution jobExec=jobExplorer.getJobExecution(executionId);
		if(jobExec == null){
			throw new NullPointerException("Job execution with id "+executionId+" was not found");
		}
		String jobName = jobExec.getJobInstance().getJobName();
		boolean isRestartable = false;
		try {
			Job job = jobRegistry.getJob(jobName);
			isRestartable = job.isRestartable();
		} catch (NoSuchJobException e) {			
		}		
		JobExecutionDto jobExecDto=SpringDtoCreatorUtils.createJobExecDtoFromSpring(jobExec);
		String code = jobExec.getExitStatus().getExitCode();
		if(code.equals(ExitStatus.STOPPED.getExitCode()) || code.equals(ExitStatus.FAILED.getExitCode())){
			jobExecDto.setRestartable(isRestartable);
		}
		return jobExecDto;
	}

	@Override
	public List<JobExecutionDto> getJobExecutions(Long jobInstanceId) {
		List<JobExecution> jobExecutions = getOriginalJobExecution(getOriginalJobInstance(jobInstanceId));
		List<JobExecutionDto> jobExecutionsDto = new ArrayList<JobExecutionDto>();
		for(JobExecution jobExecution: jobExecutions){
			jobExecutionsDto.add(SpringDtoCreatorUtils.createJobExecDtoFromSpring(jobExecution));
		}
		return jobExecutionsDto;
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
	
	/*
	public JobLoader getJobLoader() {
		return jobLoader;
	}

	public void setJobLoader(JobLoader jobLoader) {
		this.jobLoader = jobLoader;
	}
*/
	/*public JobLauncher getJobLauncher() {
		return jobLauncher;
	}

	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}*/

	/*public JobRepository getJobRepository() {
		return jobRepository;
	}

	public void setJobRepository(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}*/

	public JobRegistry getJobRegistry() {
		return jobRegistry;
	}

	public void setJobRegistry(JobRegistry jobRegistry) {
		this.jobRegistry = jobRegistry;
	}
/*
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
*/

	public JobOperator getJobOperator() {
		return jobOperator;
	}

	public void setJobOperator(JobOperator jobOperator) {
		this.jobOperator = jobOperator;
	}

	@Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		this.appContext = appContext;
		
	}

	@Override
	public JobInstanceDto getJobInstanceById(Long instanceId) {
		// TODO Auto-generated method stub
		return null;
	}

}
