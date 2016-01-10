package com.javabatchmanager.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;
import javax.batch.runtime.StepExecution;

import org.jberet.runtime.AbstractStepExecution;
import org.jberet.runtime.JobExecutionImpl;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.dtos.StepExecutionDto;

public class JSR352DtoCreatorUtils {

	/**
	 * 
	 * @param jobName
	 * @param parameters
	 * @return
	 */
	public static JobInstanceDto createJobInstanceDtoFromJSR352(JobInstance jobInstance){
		JobInstanceDto jld = new JobInstanceDto();
		jld.setJobName(jobInstance.getJobName());
		jld.setJobInstanceId(jobInstance.getInstanceId());
		jld.setJobType("jsr");
		return jld;
	}
	
	
	/**
	 * 
	 * @param jobExecution
	 * @return
	 */
	public static JobExecutionDto createJobExecDtoFromJSR352(JobExecution jobExecution){
		JobExecutionDto jed = new JobExecutionDto();
		jed.setJobName(jobExecution.getJobName());
		jed.setCreateTime(jobExecution.getCreateTime());
		jed.setStartTime(jobExecution.getStartTime());
		jed.setFinishedTime(jobExecution.getEndTime());		
		jed.setStatus(jobExecution.getBatchStatus().name());
		jed.setParameters(jobExecution.getJobParameters().toString());
		jed.setJobExecutionId(jobExecution.getExecutionId());
		if(jobExecution instanceof JobExecutionImpl){
			List<StepExecution> stepExecutions = ((JobExecutionImpl) jobExecution).getStepExecutions();
			List<StepExecutionDto> stepsDto = createStepExecutionsDto(stepExecutions);
			jed.setStepExecutionsDto(stepsDto);
		}
		jed.setJobType("jsr");
		jed.setExitDescription(jobExecution.getExitStatus());
		return jed;
	}
	
	private static List<StepExecutionDto> createStepExecutionsDto(List<StepExecution> steps){
		List<StepExecutionDto> stepExecutions = new ArrayList<StepExecutionDto>();
		for(StepExecution step: steps){
			stepExecutions.add(createStepExecutionDto(step));
		}
		return stepExecutions;
		
	}
	
	private static StepExecutionDto createStepExecutionDto(StepExecution stepExecution){
		StepExecutionDto stepDto = new StepExecutionDto();
		if(stepExecution instanceof AbstractStepExecution){
			if(((AbstractStepExecution) stepExecution).getException() != null){
				stepDto.setExitDescription(((AbstractStepExecution) stepExecution).getException().getMessage());
			}
			stepDto.setId(((AbstractStepExecution) stepExecution).getStepExecutionId());
			stepDto.setReaderCheckpointInfo(((AbstractStepExecution) stepExecution).getReaderCheckpointInfo());
			stepDto.setWriterCheckpointInfo(((AbstractStepExecution) stepExecution).getWriterCheckpointInfo());			
		}
		stepDto.setPersistentUserData(stepExecution.getPersistentUserData());
		stepDto.setEndTime(stepExecution.getEndTime());
		stepDto.setStartTime(stepExecution.getStartTime());
		stepDto.setExitStatus(stepExecution.getExitStatus());
		stepDto.setStepName(stepExecution.getStepName());
		
		return stepDto;
	}
	
	
	/**
	 * 
	 * @param jobExecs
	 * @return
	 */
	public static List<JobExecutionDto> createCollectionJobExecDtoFromJSR352(Collection<JobExecution> jobExecs){
		List<JobExecutionDto> jobExecsDto = new ArrayList<JobExecutionDto>();
		for(JobExecution jobExec: jobExecs){
			jobExecsDto.add(createJobExecDtoFromJSR352(jobExec));
		}
		return Collections.unmodifiableList(jobExecsDto);
	}
	
	public static List<JobInstanceDto> createCollectionJobInstsDtoFromJSR352(Collection<JobInstance> jobInsts){
		List<JobInstanceDto> jobInstDto = new ArrayList<JobInstanceDto>();
		for(JobInstance jobInst: jobInsts){
			jobInstDto.add(createJobInstanceDtoFromJSR352(jobInst));
		}
		return Collections.unmodifiableList(jobInstDto);
	}
}
