package com.javabatchmanager.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.batch.operations.JobExecutionAlreadyCompleteException;
import javax.batch.operations.JobExecutionNotMostRecentException;
import javax.batch.operations.JobExecutionNotRunningException;
import javax.batch.operations.JobOperator;
import javax.batch.operations.JobRestartException;
import javax.batch.operations.JobSecurityException;
import javax.batch.operations.JobStartException;
import javax.batch.operations.NoSuchJobException;
import javax.batch.operations.NoSuchJobExecutionException;
import javax.batch.operations.NoSuchJobInstanceException;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;

import org.apache.log4j.Logger;
import org.jberet.operations.JobOperatorImpl;
import org.jberet.repository.JobRepository;
import org.jberet.repository.JobRepositoryFactory;
import org.jberet.spi.BatchEnvironment;
import org.springframework.batch.support.PropertiesConverter;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Service;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.error.ExceptionCause;
import com.javabatchmanager.service.JobService;
import com.javabatchmanager.utils.JSR352DtoCreatorUtils;

/**
 * Core implementation of JobService interface, this implementations focus on
 * JSR352 jobs.
 * 
 * @author daniel
 *
 */


@Qualifier("JSR352Batch")
public class JSR352JobServiceImpl implements JobService {
	private final static Logger logger = Logger.getLogger(JSR352JobServiceImpl.class.getName());
	private JobOperator jobOperator;
	private JobRepository jobRepository;
	private BatchEnvironment batchEnvironment;

	@PostConstruct
	public void setJobOp() {
		jobOperator = (JobOperatorImpl) BatchRuntime.getJobOperator();
		ServiceLoader<BatchEnvironment> serviceLoader = ServiceLoader.load(BatchEnvironment.class);
		for (Iterator<BatchEnvironment> it = serviceLoader.iterator(); it.hasNext();) {
			batchEnvironment = it.next();
			jobRepository = JobRepositoryFactory.getJobRepository(batchEnvironment);
			break;
		}
	}

	// start
	@Override
	public JobExecutionDto start(final String jobName, final String jobParams) throws BaseBatchException {
		Properties properties = PropertiesConverter.stringToProperties(jobParams);
		JobExecutionDto jobExecDto = null;
		try {
			long executionId = jobOperator.start(jobName, properties);
			jobExecDto = JSR352DtoCreatorUtils.createJobExecDtoFromJSR352(jobOperator.getJobExecution(executionId));
		} catch (JobStartException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_START);
		} catch (JobSecurityException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_SECURITY);
		}
		return jobExecDto;
	}

	// restart
	@Override
	public JobExecutionDto restart(Long executionId) throws BaseBatchException {
		Long newExecId = null;
		JobExecution jobExec = jobOperator.getJobExecution(executionId);
		if (jobExec == null) {
			throw new BaseBatchException("JobExecution doesn't exists.", ExceptionCause.NO_SUCH_JOB_EXECUTION);
		}
		try {
			if (jobExec.getBatchStatus() == BatchStatus.FAILED || jobExec.getBatchStatus() == BatchStatus.STOPPED) {
				newExecId = jobOperator.restart(executionId,
						jobOperator.getJobExecution(executionId).getJobParameters());
			}
		} catch (JobExecutionAlreadyCompleteException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_EXECUTION_ALREADY_COMPLETE);
		} catch (JobExecutionNotMostRecentException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_EXECUTION_NOT_MOST_RECENT);
		} catch (JobSecurityException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_SECURITY);
		} catch (JobRestartException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_RESTART);
		}
		if (newExecId != null) {
			jobExec = jobOperator.getJobExecution(newExecId);
		}
		return JSR352DtoCreatorUtils.createJobExecDtoFromJSR352(jobExec);
	}

	// stop
	@Override
	public JobExecutionDto stop(Long executionId) throws BaseBatchException {
		if("STOPPING".equals(getJobExecutionById(executionId).getStatus())){
			return getJobExecutionById(executionId);
		}
		try {
			jobOperator.stop(executionId);
		} catch (NoSuchJobExecutionException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.NO_SUCH_JOB_EXECUTION);
		} catch (JobExecutionNotRunningException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_EXECUTION_NOT_RUNNING);
		} catch (JobSecurityException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_SECURITY);
		}
		JobExecution jobExec = jobOperator.getJobExecution(executionId);
		return JSR352DtoCreatorUtils.createJobExecDtoFromJSR352(jobExec);
	}

	// ----------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------
	/*
	 * Get Dtos executions and instances methods
	 */
	// ----------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------------------------

	@Override
	public Set<String> getJobNames() {
		File[] jobDefinitions = null;
		Set<String> jobNames = new HashSet<>();
		String path = System.getProperty("user.home") + File.separator + "javaBatchManUpJobs" + File.separator
				+ "uploadedJobContexts"+ File.separator+"jsrJobs";

		File fileMetaInf = new File(path);
		jobDefinitions = fileMetaInf.listFiles();
		if (jobDefinitions != null) {
			for (File jobDef : jobDefinitions) {
				if (jobDef.getName().contains(".xml")) {
					jobNames.add(jobDef.getName().replaceAll(".xml", ""));
				}
			}
		}
		jobNames.addAll(jobOperator.getJobNames());

		return jobNames;
	}
	
	@Override
	public List<JobExecutionDto> getJobExecutions(JobInstanceDto jobInstance) throws BaseBatchException {
		JobInstance jobInst = null;
		if(jobInstance == null){
			return new ArrayList<JobExecutionDto>(); 
		}
		
		try {
			jobInst = jobRepository.getJobInstance(jobInstance.getJobInstanceId());
			if (jobInst == null) {
				return new ArrayList<JobExecutionDto>();
			}
			List<JobExecution> jobExecs = jobOperator.getJobExecutions(jobInst);
			return JSR352DtoCreatorUtils.createCollectionJobExecDtoFromJSR352(jobExecs);
		} catch (NoSuchJobInstanceException e) {
			// should never be thrown, because we are retrieving job Instance
			// from repo.
			System.err.println(e.getMessage());
			return new ArrayList<JobExecutionDto>();
		} catch (JobSecurityException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_SECURITY);
		}

	}

	@Override
	public List<JobInstanceDto> getJobInstances(String jobName, int start, int count) throws BaseBatchException {
		try {
			List<JobInstance> jobInstances = jobOperator.getJobInstances(jobName, start, count);
			List<JobInstanceDto> jobInstsDto = JSR352DtoCreatorUtils
					.createCollectionJobInstsDtoFromJSR352(jobInstances);
			return jobInstsDto;
		} catch (NoSuchJobException e) {
			return new ArrayList<JobInstanceDto>();
		} catch (JobSecurityException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_SECURITY);
		}
	}


	@Override
	public JobExecutionDto getJobExecutionById(Long executionId) throws BaseBatchException {
		try {
			JobExecution jobExec = jobOperator.getJobExecution(executionId);
			JobExecutionDto jobExecDto = JSR352DtoCreatorUtils.createJobExecDtoFromJSR352(jobExec);
			if (jobExec.getBatchStatus() == BatchStatus.FAILED || jobExec.getBatchStatus() == BatchStatus.STOPPED) {
				jobExecDto.setRestartable(true);
			}
			return jobExecDto;
		} catch (NoSuchJobExecutionException | NullPointerException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.NO_SUCH_JOB_EXECUTION);
		} catch (JobSecurityException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_SECURITY);
		} 
	}

	@Override
	public Set<JobExecutionDto> getRunningJobExecutions(String jobName) throws BaseBatchException {
		List<Long> jobExecs;
		try {
			jobExecs = jobOperator.getRunningExecutions(jobName);
		} catch (NoSuchJobException e) {
			return new HashSet<JobExecutionDto>();
		} catch (JobSecurityException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_SECURITY);
		}
		Set<JobExecutionDto> jobExecsDto = new HashSet<JobExecutionDto>();
		for (Long id : jobExecs) {
			jobExecsDto.add(getJobExecutionById(id));
		}
		return jobExecsDto;
	}

	@Override
	public List<JobExecutionDto> getAllRunningJobExecutions() throws BaseBatchException {
		Set<String> jobNames = getJobNames();
		List<JobExecutionDto> runningExecsDto = new ArrayList<JobExecutionDto>();
		for (String jobName : jobNames) {
			runningExecsDto.addAll(getRunningJobExecutions(jobName));
		}
		return runningExecsDto;
	}

}
