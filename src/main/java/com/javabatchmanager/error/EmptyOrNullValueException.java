package com.javabatchmanager.error;

public class EmptyOrNullValueException extends RestException{
	
	public EmptyOrNullValueException(String msg){
		super(msg);
	}

	public EmptyOrNullValueException(String msg, Throwable e){
		super(msg,e);
	}
	
	public EmptyOrNullValueException(String msg,Throwable e, String [] wrongParams, String errorCode){
		super(msg,e,wrongParams,errorCode);
	}
}

