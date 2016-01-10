package com.javabatchmanager.web;

import com.javabatchmanager.error.BaseBatchException;
import org.springframework.validation.BindingResult;

 

public class ControllerExceptionHandler {

	public static void handleException(BaseBatchException e, BindingResult error, Object[] args){
		error.rejectValue("jobName", e.getCauseEnum().getErrCode(),args,null);
	}
}
