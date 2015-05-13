package com.javabatchmanager.dao;

import com.javabatchmanager.dtos.JobSummary;

public interface JobSummaryDao {
	
	public JobSummary create(JobSummary jobSum);
	public void delete(Long Id);
	public JobSummary update(JobSummary jobSum);
	public JobSummary getById(Long Id);

}
