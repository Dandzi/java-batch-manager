package com.javabatchmanager.watchers;

import javax.annotation.PostConstruct;
import javax.batch.api.listener.JobListener;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.javabatchmanager.websocket.JobNotificationEndpoint;

public class JSR352JobListener implements JobListener{
	private final static Logger logger = Logger.getLogger(JSR352JobListener.class.getName());

	public static Logger getLogger() {
		return logger;
	}

	@Override
	public void beforeJob() throws Exception {
		JobNotificationEndpoint.addMessage("{\"JSR352msg\":\"JSR352 jobExecution is starting\"}");
	}

	@Override
	public void afterJob() throws Exception {
		JobNotificationEndpoint.addMessage("{\"JSR352msg\":\"JSR352 jobExecution is stopping\"}");		
	}


}
