package com.javabatchmanager.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping(value = "/rest-docs")
public class JobRestDocsController {
	
	@RequestMapping(method = RequestMethod.GET)
	public String getView(){
		return "rest-docs";
	}
}
