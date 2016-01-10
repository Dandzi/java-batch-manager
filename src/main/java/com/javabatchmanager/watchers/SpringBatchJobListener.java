package com.javabatchmanager.watchers;



import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.utils.SpringDtoCreatorUtils;
import com.javabatchmanager.websocket.JobNotificationEndpoint;

public class SpringBatchJobListener implements JobExecutionListener{
	private final static Logger logger = Logger.getLogger(SpringBatchJobListener.class.getName());
	
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		logger.info("BeforeJob - notifying: "+jobExecution.getJobInstance().getJobName()+" is starting.");
		try {
			Thread.sleep(1000*4);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		JobNotificationEndpoint.addMessage(createMsg(jobExecution));

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		logger.info("AfterJob - notifying: "+jobExecution.getJobInstance().getJobName() +" is ending.");		
		JobNotificationEndpoint.addMessage(createMsg(jobExecution));
	}
	public JobExecutionDto createMsg(JobExecution jobExecution){
		return SpringDtoCreatorUtils.createJobExecDtoFromSpring(jobExecution);
	}


}
