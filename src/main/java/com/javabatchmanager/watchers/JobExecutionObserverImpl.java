package com.javabatchmanager.watchers;

import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Component;

import com.javabatchmanager.websocket.JobNotificationEndpoint;

@Component
public class JobExecutionObserverImpl implements JobExecutionObserver{

	private final static Logger logger = Logger.getLogger(JobExecutionObserverImpl.class.getName());
	
	//@Autowired
	//private MessageSendingOperations<String> messagingTemplate;
	@Autowired
	private JobNotificationEndpoint jobNotification;
	
	private boolean connected=false;
	
	//public JobExecutionObserver(
	//		MessageSendingOperations<String> messagingTemplate) {
	//	super();
	//	this.messagingTemplate = messagingTemplate;
	//}

	@Monitor
	public void update(Object arg) {
		logger.info("Will try to send message to client");
		while(!isConnected()){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("Sending ws message to client");
		jobNotification.notifyUser((String) arg); 
	}

	public boolean isConnected() {
		return connected;
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	/*public MessageSendingOperations<String> getMessagingTemplate() {
		return messagingTemplate;
	}

	public void setMessagingTemplate(
			MessageSendingOperations<String> messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}*/

	public JobNotificationEndpoint getJobNotification() {
		return jobNotification;
	}

	public void setJobNotification(JobNotificationEndpoint jobNotification) {
		this.jobNotification = jobNotification;
	}
	
	

}
