package com.javabatchmanager.service.impl;

import java.io.Serializable;



import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;

import com.javabatchmanager.dao.JobSummaryDao;
import com.javabatchmanager.dtos.JobSummary;
import com.javabatchmanager.service.JobSummaryService;

//@Transactional
public class JobSummaryServiceImpl implements JobSummaryService, Serializable{
	
	//@Inject
	private JobSummaryDao jobSumDao;
	

	
	
	public JobSummary createJobSummary(JobSummary jobSum)
	{
		return jobSumDao.create(jobSum);		
	}
	
	/*public JobExecution launch(String jobName){
		//JobExecution jobExecution = null;
		//SimpleJob job = 
		//JobParameters jobParameters = new JobParameters();
		//jobExecution = jobLauncher.run(arg0, arg1)
		return jobExecution;
	}*/
}
