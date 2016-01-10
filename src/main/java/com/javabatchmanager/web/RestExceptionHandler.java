package com.javabatchmanager.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.javabatchmanager.error.ErrorResource;
import com.javabatchmanager.error.RestException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
	
	private Properties properties = new Properties();
	
	@ExceptionHandler({RestException.class})
	public ResponseEntity<Object> handleInvalidRequest(RestException ex, WebRequest request){
		try{
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("view.properties");
			properties.load(inputStream);
		} catch(IOException ioEx){
			System.out.println(ioEx);
		}
		ErrorResource errRes = buildError(ex.getErrorCode(), ex.getWrongParameters(), ex);
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		return handleExceptionInternal(ex, errRes, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	private ErrorResource buildError(String code, Object[] args, Throwable ex){
		String msg = MessageFormat.format(properties.getProperty(code), args);
		ErrorResource errRes =null;
		if(ex.getCause()!=null){
			errRes = new ErrorResource(msg, ExceptionUtils.getRootCause(ex).toString(), ex.getMessage());
		}else{
			errRes = new ErrorResource(msg);
		}
		return errRes;
	}
}
