package com.javabatchmanager.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.javabatchmanager.service.JobService;

public abstract class AbstractController {
	
	@Autowired
	@Qualifier("JSR352Batch")
	protected JobService jobServiceJSR;
	
	@Autowired
	@Qualifier("SpringBatch")
	protected JobService jobServiceSpring;	
	
	protected JobService getJobService(String serviceType){
		if(serviceType.equals("spring")){
			return jobServiceSpring;
		}
		return jobServiceJSR;
	}
	
}
