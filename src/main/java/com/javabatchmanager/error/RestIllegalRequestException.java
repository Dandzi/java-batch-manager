package com.javabatchmanager.error;

import org.springframework.batch.core.JobExecutionException;

@SuppressWarnings("serial")
public class RestIllegalRequestException extends RestException{
	
	
	public RestIllegalRequestException(String s,Throwable e, String [] wrongParameters, String errorCode) {
		super(s,e,wrongParameters,errorCode);
	}
}
