package com.javabatchmanager.websocket;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.service.JobService;
import com.javabatchmanager.watchers.JobExecutionObserver;

@Controller
public class JobNotificationEndpoint {
	
	private final static Logger logger = Logger.getLogger(JobNotificationEndpoint.class.getName());
	
	@Autowired
	private MessageSendingOperations<String> messagingTemplate;
	private boolean connected;
	
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
			notifyUser("{\"action\":\"reload\"}");
		}
		return;
	}
	
	public void notifyUser(Object notification){
		logger.info("Will try to send message to client");
		while(!connected){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("Sending ws message to client");
		messagingTemplate.convertAndSend("/launchable-jobs",notification);
	}

}
