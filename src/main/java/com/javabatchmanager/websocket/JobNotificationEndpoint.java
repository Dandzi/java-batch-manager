package com.javabatchmanager.websocket;


import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class JobNotificationEndpoint {
	
	private final static Logger logger = Logger.getLogger(JobNotificationEndpoint.class.getName());
	
	@Autowired
	private MessageSendingOperations<String> messagingTemplate;
	private static boolean connected;
	private static List<String> jsr352Msgs;
	private static JobNotificationEndpoint endpoint;

	@PostConstruct
	private void setJobNotification(){
		endpoint = this;
	}
	
	@MessageMapping("/notification")
	@SendTo("/launchable-jobs")
	public void updateJobNotification(final String message) throws InterruptedException {
		logger.info("Websocket message received: "+message);
		if(message.equals("connected")){
			connected=true;
		}
		if(message.equals("disconnected")){
			connected=false;
		}
		if(message.equals("reload")){
			connected=false;
			addMessage("{\"action\":\"reload\"}");
		}
		return;
	}
	
	private void notifyUser(Object notification){
		messagingTemplate.convertAndSend("/launchable-jobs",notification);
	}
	
	
	public static void addMessage(Object notification){
		int i=0;
		while(!connected && i<5){
			try {
				i++;
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(i==4){ //connection was not established - not sending msg;
			return;
		}
		logger.info("Sending ws message to client");
		endpoint.notifyUser(notification);
	}

	public MessageSendingOperations<String> getMsgTempl(){
		return messagingTemplate;
	}

}
