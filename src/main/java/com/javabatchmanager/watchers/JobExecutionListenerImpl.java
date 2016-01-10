package com.javabatchmanager.watchers;

import java.util.Observer;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.service.JobService;

//@Component
public class JobExecutionListenerImpl extends Listener implements JobExecutionListener{
	private final static Logger logger = Logger.getLogger(JobExecutionListenerImpl.class.getName());
	private ObservableJobExecution jobs = new ObservableJobExecution();
	private JobExecutionDto jobExecdto = new JobExecutionDto();
	//private final Object lock;
	@Autowired
	private JobExecutionObserver jobExecutionObserver;
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		logger.info("BeforeJob method start.");
		/*synchronized(lock){
			if(jobExecutionObserver == null){
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
		}*/
		synchronized(this){
			jobs.addObserver(jobExecutionObserver);
			setJobExecutionDto(jobExecution);
			//jobs.setValue();
			//jobs.notifyObservers(jobExecdto);
			logger.info("BeforeJob observer notified.");
		}
		logger.info("BeforeJob method end.");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		logger.info("AfterJob method start.");
		synchronized(this){
			//jobs.setValue();
			setJobExecutionDto(jobExecution);
			//jobs.notifyObservers(jobExecdto);
			logger.info("AfterJob observer notified");
		}
	}
	public void setJobExecutionDto(JobExecution jobExecution){
		jobExecdto.setJobName(jobExecution.getJobInstance().getJobName());
		jobExecdto.setParameters(jobExecution.getJobParameters().toString());
		jobExecdto.setStatus(jobExecution.getStatus().toString());
}

	//public Observer getJobExecutionObserver() {
	//	return jobExecutionObserver;
	//}

	public void setJobExecutionObserver(JobExecutionObserver jobExecutionObserver) {
		this.jobExecutionObserver = jobExecutionObserver;
	}
}
