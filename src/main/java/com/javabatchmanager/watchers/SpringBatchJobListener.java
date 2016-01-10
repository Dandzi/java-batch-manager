package com.javabatchmanager.watchers;



import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.utils.SpringDtoCreatorUtils;
import com.javabatchmanager.websocket.JobNotificationEndpoint;

public class SpringBatchJobListener implements JobExecutionListener{
	private final static Logger logger = Logger.getLogger(JobExecutionListenerImpl.class.getName());
	private JobNotificationEndpoint notification;
	
	public SpringBatchJobListener(JobNotificationEndpoint notification) {
		this.notification = notification;
	}
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		logger.info("BeforeJob - notifying: "+jobExecution.getJobInstance().getJobName()+" is starting.");		
		notification.notifyUser(createMsg(jobExecution));

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		logger.info("AfterJob - notifying: "+jobExecution.getJobInstance().getJobName() +" is ending.");		
		notification.notifyUser(createMsg(jobExecution));
	}
	public JobExecutionDto createMsg(JobExecution jobExecution){
		return SpringDtoCreatorUtils.createJobExecDtoFromSpring(jobExecution);
	}


}
