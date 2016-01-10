package com.javabatchmanager.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


import java.util.Set;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstanceDto;

public final class SpringDtoCreatorUtils {

	private SpringDtoCreatorUtils() {
	}
	
	
	/**
	 * 
	 * @param jobName
	 * @param parameters
	 * @param id
	 * @return
	 */
	public static JobInstanceDto createJobInstanceDtoFromSpring(String jobName, String parameters, Long id){
		JobInstanceDto jid = new JobInstanceDto();
		jid.setJobName(jobName);
		jid.setParameters(parameters);
		jid.setJobInstanceId(id);
		return jid;
	}
	
		
	/**
	 * 
	 * @param jobInst
	 * @return
	 */
	public static JobInstanceDto createJobInstanceDtoFromSpring(org.springframework.batch.core.JobInstance jobInst){
		JobInstanceDto jid = new JobInstanceDto();
		jid.setJobInstanceId(jobInst.getId());
		jid.setJobName(jobInst.getJobName());
		return jid;
	}
	
	/**
	 * 
	 * @param jobInsts
	 * @return
	 */
	public static Collection<JobInstanceDto> createCollectionJobInstDtoFromSpring(Collection<org.springframework.batch.core.JobInstance> jobInsts){
		Collection<JobInstanceDto> jobInstDto = new ArrayList<JobInstanceDto>();
		for(org.springframework.batch.core.JobInstance jobInst: jobInsts){
			jobInstDto.add(createJobInstanceDtoFromSpring(jobInst));
		}
		return jobInstDto;
	}
	

	/**
	 * 
	 * @param jobExecution
	 * @return
	 */
	public static JobExecutionDto createJobExecDtoFromSpring(org.springframework.batch.core.JobExecution jobExecution){
		JobExecutionDto jed = new JobExecutionDto();
		jed.setJobName(jobExecution.getJobInstance().getJobName());
		jed.setCreateTime(jobExecution.getCreateTime());
		jed.setStartTime(jobExecution.getStartTime());
		jed.setStatus(jobExecution.getStatus().toString());
		jed.setParameters(jobExecution.getJobParameters().toString());
		jed.setJobExecutionId(jobExecution.getId());
		return jed;
	}
	

	/**
	 * 
	 * @param jobExecs
	 * @return
	 */
	public static Set<JobExecutionDto> createCollectionJobExecDtoFromSpring(Collection<org.springframework.batch.core.JobExecution> jobExecs){
		Set<JobExecutionDto> jobExecsDto = new HashSet<JobExecutionDto>();
		for(org.springframework.batch.core.JobExecution jobExec: jobExecs){
			jobExecsDto.add(createJobExecDtoFromSpring(jobExec));
		}
		return Collections.unmodifiableSet(jobExecsDto);
	}
	

	
}
