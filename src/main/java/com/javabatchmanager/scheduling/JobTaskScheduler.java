package com.javabatchmanager.scheduling;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.support.PropertiesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.javabatchmanager.dtos.JobInstanceDto;

@Component
public class JobTaskScheduler {
	
	private TaskScheduler taskScheduler;

	private JobOperator jobOperator;
	
	private JobRepository jobRepo;
	
	private JobRegistry jobRegistry;
	
	private JobParametersConverter jobParametersConverter;
	
	private JobExecutionListener jobExecutionListener;
	
	//TODO registered listener??
	public void launch(final JobInstanceDto jobInst, Date startTime){
		try {
			JobParameters jobParams=jobParametersConverter.getJobParameters(PropertiesConverter.stringToProperties(jobInst.getParameters()));
			final JobExecution jobExecution = jobRepo.createJobExecution(jobInst.getJobName(), jobParams);
			taskScheduler.schedule(new Runnable(){

				@Override
				public void run() {
					try {
						AbstractJob job = (AbstractJob) jobRegistry.getJob(jobInst.getJobName());
						job.registerJobExecutionListener(jobExecutionListener);
						job.execute(jobExecution);
					} catch (NoSuchJobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}}, startTime);
			
		} catch (JobExecutionAlreadyRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobRestartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobInstanceAlreadyCompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}

	public void setTaskScheduler(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
	}

	public JobOperator getJobOperator() {
		return jobOperator;
	}

	public void setJobOperator(JobOperator jobOperator) {
		this.jobOperator = jobOperator;
	}

	public JobParametersConverter getJobParametersConverter() {
		return jobParametersConverter;
	}

	public void setJobParametersConverter(JobParametersConverter jobParametersConverter) {
		this.jobParametersConverter = jobParametersConverter;
	}
	
}
