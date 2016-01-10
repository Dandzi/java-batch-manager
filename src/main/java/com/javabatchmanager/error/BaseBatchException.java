package com.javabatchmanager.error;



public class BaseBatchException extends Exception{

	public ExceptionCause causeEnum;

	public BaseBatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseBatchException(String message) {
		super(message);
	}
	
	public BaseBatchException(String message,ExceptionCause causeEnum) {
		super(message);
		this.causeEnum=causeEnum;
	}
	
	public BaseBatchException(String message, Throwable cause,ExceptionCause causeEnum) {
		super(message, cause);
		this.causeEnum=causeEnum;
	}

	public ExceptionCause getCauseEnum() {
		return causeEnum;
	}

	public void setCauseEnum(ExceptionCause causeEnum) {
		this.causeEnum = causeEnum;
	}
}
