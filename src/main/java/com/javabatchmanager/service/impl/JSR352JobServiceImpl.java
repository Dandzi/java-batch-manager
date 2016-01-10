package com.javabatchmanager.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;

import org.apache.log4j.Logger;
import org.jberet.creation.ArchiveXmlLoader;
import org.jberet.creation.ArtifactFactoryWrapper;
import org.jberet.repository.JobRepository;
import org.jberet.repository.JobRepositoryFactory;
import org.jberet.spi.BatchEnvironment;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.support.PropertiesConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.dtos.JobInstanceWrapper;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.error.ExceptionCause;
import com.javabatchmanager.service.JobService;
import com.javabatchmanager.utils.JSR352DtoCreatorUtils;
import com.javabatchmanager.utils.SpringDtoCreatorUtils;
import com.javabatchmanager.watchers.Listener;
import com.javabatchmanager.web.JobLauncherController;

@Service
@Qualifier("JSR352Batch")
public class JSR352JobServiceImpl implements JobService {
	private final static Logger logger = Logger.getLogger(JSR352JobServiceImpl.class.getName());
	private JobOperator jobOperator;
	private JobRepository jobRepository;

	@PostConstruct
	private void setJobOp() {
		jobOperator = BatchRuntime.getJobOperator();
        ServiceLoader<BatchEnvironment> serviceLoader = ServiceLoader.load(BatchEnvironment.class);
        for (Iterator<BatchEnvironment> it = serviceLoader.iterator(); it.hasNext(); ) {
        	BatchEnvironment batchEnvironment = it.next();
            jobRepository = JobRepositoryFactory.getJobRepository(batchEnvironment);
            break;
        }
	}

	@Override
	public JobExecutionDto start(String jobName, String jobParams) throws BaseBatchException {
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

	@Override
	public JobExecutionDto restart(Long executionId) throws BaseBatchException {
		Long newExecId = null;
		jobOperator.getJobExecution(executionId);
		try {
			newExecId = jobOperator.restart(executionId, jobOperator.getJobExecution(executionId).getJobParameters());
		} catch (JobExecutionAlreadyCompleteException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_EXECUTION_ALREADY_COMPLETE);
		} catch (NoSuchJobExecutionException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.NO_SUCH_JOB_EXECUTION);
		} catch (JobExecutionNotMostRecentException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_EXECUTION_NOT_MOST_RECENT);
		} catch (JobSecurityException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_SECURITY);
		} catch (JobRestartException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_RESTART);
		}
		JobExecution jobExec = jobOperator.getJobExecution(newExecId);
		return JSR352DtoCreatorUtils.createJobExecDtoFromJSR352(jobExec);
	}

	@Override
	public JobExecutionDto stop(Long executionId) throws BaseBatchException {
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
	public Set<String> getJobNames() throws BaseBatchException {
		File[] jobDefinitions = null;
		Set<String> jobNames = new HashSet<>();
		try {
			Enumeration<URL> en=getClass().getClassLoader().getResources("META-INF/batch-jobs");
			URL metaInf=en.nextElement();
			File fileMetaInf=new File(metaInf.getPath());
			jobDefinitions=fileMetaInf.listFiles();
			for (File jobDef : jobDefinitions) {
				jobNames.add(jobDef.getName().replaceAll(".xml", ""));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return jobNames;
		}
		return jobNames;
	}


	@Override
	public List<JobExecutionDto> getJobExecutions(JobInstanceDto jobInstance) throws BaseBatchException {
		List<JobInstance> jobInst = null;
		try {
			jobInst = jobOperator.getJobInstances(jobInstance.getJobName(), (int) (long) jobInstance.getJobInstanceId(), 1);
			if (jobInst == null || jobInst.isEmpty()) {
				return new ArrayList<JobExecutionDto>();
			}
			List<JobExecution> jobExecs = jobOperator.getJobExecutions(jobInst.get(0));
			return (List<JobExecutionDto>) JSR352DtoCreatorUtils.createCollectionJobExecDtoFromJSR352(jobExecs);
		} catch (NoSuchJobException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.NO_SUCH_JOB);
		} catch (NoSuchJobInstanceException e){
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.NO_SUCH_JOB_INSTANCE);		
		} catch (JobSecurityException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_SECURITY);
		}
		
	}

	@Override
	public List<JobInstanceDto> getJobInstances(String jobName, int start, int count) throws BaseBatchException {
		try{
			List<JobInstance> jobInstances = jobOperator.getJobInstances(jobName, start, count);
			List<JobInstanceDto> jobInstsDto = (List<JobInstanceDto>) JSR352DtoCreatorUtils
				.createCollectionJobInstsDtoFromJSR352(jobInstances);
			return jobInstsDto;
		} catch (NoSuchJobException e){
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.NO_SUCH_JOB);
		} catch (JobSecurityException e){
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_SECURITY);
		}		
	}

	@Override
	public JobExecutionDto getJobExecutionById(Long executionId) throws BaseBatchException {
		try{
			JobExecution jobExec = jobOperator.getJobExecution(executionId);
			JobExecutionDto jobExecDto = JSR352DtoCreatorUtils.createJobExecDtoFromJSR352(jobExec);
			return jobExecDto;
		} catch (NoSuchJobExecutionException e){
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.NO_SUCH_JOB_EXECUTION);
		} catch (JobSecurityException e){
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_SECURITY);
		}
	}

	@Override
	public Set<JobExecutionDto> getRunningJobExecutions(String jobName) throws BaseBatchException {
		List<Long> jobExecs;
		try {
			jobExecs = jobOperator.getRunningExecutions(jobName);
		} catch (NoSuchJobException e) {
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.NO_SUCH_JOB);
		} catch (JobSecurityException e){
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

	@Override
	public JobInstanceDto getJobInstanceById(Long jobInstanceId) {
		JobInstance jobInstance = jobRepository.getJobInstance(jobInstanceId);
		JobInstanceDto jobInstanceDto =  JSR352DtoCreatorUtils.createJobInstanceDtoFromJSR352(jobInstance);		
		return jobInstanceDto;
	}

	@Override
	public List<JobExecutionDto> getJobExecutions(Long jobInstanceId) throws BaseBatchException {
		try{
			JobInstance jobInstance = jobRepository.getJobInstance(jobInstanceId);
			List<JobExecution> jobExecutionList=jobOperator.getJobExecutions(jobInstance);
			JSR352DtoCreatorUtils.createCollectionJobExecDtoFromJSR352(jobExecutionList);
			return JSR352DtoCreatorUtils.createCollectionJobExecDtoFromJSR352(jobExecutionList);
		} catch (NoSuchJobExecutionException e){
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.NO_SUCH_JOB_EXECUTION);
		} catch (JobSecurityException e){
			throw new BaseBatchException(e.getMessage(), e, ExceptionCause.JOB_SECURITY);
		}
	}

	@Override
	public List<JobParameters> getJobParametersOfJobInstance(org.springframework.batch.core.JobInstance jobInstance) {		
		return null;
	}
}
