package com.javabatchmanager.scheduling;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.error.RestException;
import com.javabatchmanager.service.JobService;

@Qualifier("JobTaskScheduler")
public class JobTaskScheduler {

	private TaskScheduler taskScheduler;

	private JobService jobServiceJSR;

	private JobService jobServiceSpring;

	private List<JobInstanceDto> futureJobs = new ArrayList<>();
	public void launch(final JobInstanceDto jobInst, Date startTime) {
		futureJobs.add(jobInst);
		taskScheduler.schedule(new Runnable() {
			@Override
			public void run() {
				String[] parsedJobName = jobInst.getJobName().split("_");
				try {
					JobService jobService = getJobServiceByType(parsedJobName[1]);
					futureJobs.remove(jobInst);
					jobService.start(parsedJobName[0], jobInst.getParameters());					
				} catch (BaseBatchException e) {
					System.err.println(e.getMessage());
				}

			}
		}, startTime);
	}

	public List<JobInstanceDto> getFutureJobs(){
		return futureJobs;
	}
	
	public TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}

	public void setTaskScheduler(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
	}

	private JobService getJobServiceByType(String type) throws BaseBatchException {
		if ("jsr".equals(type)) {
			return jobServiceJSR;
		}
		if ("spring".equals(type)) {
			return jobServiceSpring;
		}
		throw new BaseBatchException("Type " + type + " of jobService does not exists.");
	}

	
	public JobService getJobServiceJSR() {
		return jobServiceJSR;
	}

	public void setJobServiceJSR(JobService jobServiceJSR) {
		this.jobServiceJSR = jobServiceJSR;
	}

	public JobService getJobServiceSpring() {
		return jobServiceSpring;
	}

	public void setJobServiceSpring(JobService jobServiceSpring) {
		this.jobServiceSpring = jobServiceSpring;
	}
}
