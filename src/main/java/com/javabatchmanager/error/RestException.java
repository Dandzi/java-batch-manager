package com.javabatchmanager.error;

import org.springframework.batch.core.JobExecutionException;

@SuppressWarnings("serial")
public class RestException extends Exception{
	private String[] wrongParameters;
	private String errorCode;
	
	public RestException(String s){
		super(s);
	};
	
	public RestException(String s,Throwable e){
		super(s,e);
	};
	
	public RestException(String s,Throwable e, String [] wrongParameters, String errorCode) {
		super(s,e);
		this.setWrongParameters(wrongParameters);
		this.setErrorCode(errorCode);
	}
	
	public String[] getWrongParameters() {
		return wrongParameters;
	}

	public void setWrongParameters(String[] wrongParameters) {
		this.wrongParameters = wrongParameters;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
