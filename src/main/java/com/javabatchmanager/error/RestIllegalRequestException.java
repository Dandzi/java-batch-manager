package com.javabatchmanager.error;


@SuppressWarnings("serial")
public class RestIllegalRequestException extends RestException{
	
	public RestIllegalRequestException(String s){
		super(s);
	};
	
	public RestIllegalRequestException(String s,Throwable e, String [] wrongParameters, String errorCode) {
		super(s,e,wrongParameters,errorCode);
	}
	
}
