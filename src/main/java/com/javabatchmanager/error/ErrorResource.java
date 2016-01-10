package com.javabatchmanager.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResource {
	private String message;
	private String nestedException="No nested exception";
	private String nestedMessage="No nested message";
	
    public ErrorResource() { }

    public ErrorResource(String message) {
        this.message = message;
    }
	
    public ErrorResource(String message, String nestedExcpetion, String nestedMessage) {
    	this.message=message;
    	this.setNestedException(nestedExcpetion);
    	this.setNestedMessage(nestedMessage);
    }
    
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNestedException() {
		return nestedException;
	}

	public void setNestedException(String nestedException) {
		this.nestedException = nestedException;
	}

	public String getNestedMessage() {
		return nestedMessage;
	}

	public void setNestedMessage(String nestedMessage) {
		this.nestedMessage = nestedMessage;
	}
}
