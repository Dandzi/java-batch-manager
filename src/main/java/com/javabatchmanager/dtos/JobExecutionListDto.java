package com.javabatchmanager.dtos;

import java.util.List;

/*
 * JobExecution wrapper for list of jobExecution
 */
public class JobExecutionListDto {
	private List<JobExecutionDto> allJobExecutions;

	public List<JobExecutionDto> getAllJobExecutions() {
		return allJobExecutions;
	}

	public void setAllJobExecutions(List<JobExecutionDto> allJobExecutions) {
		this.allJobExecutions = allJobExecutions;
	}
	
	
}
