package com.javabatchmanager.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.dtos.StepExecutionDto;

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
	public static JobInstanceDto createJobInstanceDtoFromSpring(String jobName, String parameters, Long id) {
		JobInstanceDto jid = new JobInstanceDto();
		jid.setJobName(jobName);
		jid.setParameters(parameters);
		jid.setJobInstanceId(id);
		jid.setJobType("spring");
		return jid;
	}

	/**
	 * 
	 * @param jobInst
	 * @return
	 */
	public static JobInstanceDto createJobInstanceDtoFromSpring(JobInstance jobInst) {
		JobInstanceDto jid = new JobInstanceDto();
		jid.setJobInstanceId(jobInst.getId());
		jid.setJobName(jobInst.getJobName());
		jid.setJobType("spring");
		return jid;
	}

	/**
	 * 
	 * @param jobInsts
	 * @return
	 */
	public static Collection<JobInstanceDto> createCollectionJobInstDtoFromSpring(Collection<JobInstance> jobInsts) {
		Collection<JobInstanceDto> jobInstDto = new ArrayList<JobInstanceDto>();
		for (org.springframework.batch.core.JobInstance jobInst : jobInsts) {
			jobInstDto.add(createJobInstanceDtoFromSpring(jobInst));
		}
		return jobInstDto;
	}

	/**
	 * 
	 * @param jobExecution
	 * @return
	 */
	public static JobExecutionDto createJobExecDtoFromSpring(JobExecution jobExecution) {
		JobExecutionDto jed = new JobExecutionDto();
		jed.setJobName(jobExecution.getJobInstance().getJobName());
		jed.setCreateTime(jobExecution.getCreateTime());
		jed.setStartTime(jobExecution.getStartTime());
		jed.setFinishedTime(jobExecution.getEndTime());
		jed.setStatus(jobExecution.getStatus().toString());
		jed.setParameters(jobExecution.getJobParameters().toString());
		jed.setJobExecutionId(jobExecution.getId());
		List<StepExecution> stepExecutions = (List<StepExecution>) jobExecution.getStepExecutions();
		List<StepExecutionDto> stepsDto = createStepExecutionsDto(stepExecutions);
		jed.setStepExecutionsDto(stepsDto);
		jed.setExitDescription(jobExecution.getExitStatus().getExitDescription());
		jed.setJobType("spring");
		return jed;
	}

	private static List<StepExecutionDto> createStepExecutionsDto(List<StepExecution> steps) {
		List<StepExecutionDto> stepExecutions = new ArrayList<StepExecutionDto>();
		for (StepExecution step : steps) {
			stepExecutions.add(createStepExecutionDto(step));
		}
		return stepExecutions;

	}

	private static StepExecutionDto createStepExecutionDto(StepExecution stepExecution) {
		StepExecutionDto stepDto = new StepExecutionDto();
		stepDto.setExitDescription(stepExecution.getExitStatus().getExitDescription());
		stepDto.setId(stepExecution.getId());
		stepDto.setEndTime(stepExecution.getEndTime());
		stepDto.setStartTime(stepExecution.getStartTime());
		stepDto.setExitStatus(stepExecution.getExitStatus().getExitCode());
		stepDto.setStepName(stepExecution.getStepName());
		return stepDto;
	}

	/**
	 * 
	 * @param jobExecs
	 * @return
	 */
	public static Set<JobExecutionDto> createCollectionJobExecDtoFromSpring(Collection<JobExecution> jobExecs) {
		Set<JobExecutionDto> jobExecsDto = new HashSet<JobExecutionDto>();
		for (JobExecution jobExec : jobExecs) {
			jobExecsDto.add(createJobExecDtoFromSpring(jobExec));
		}
		return Collections.unmodifiableSet(jobExecsDto);
	}

}
