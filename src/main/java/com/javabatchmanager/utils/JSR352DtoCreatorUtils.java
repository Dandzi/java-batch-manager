package com.javabatchmanager.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstanceDto;

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
		jed.setStatus(jobExecution.getExitStatus());
		jed.setParameters(jobExecution.getJobParameters().toString());
		jed.setJobExecutionId(jobExecution.getExecutionId());
		return jed;
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
