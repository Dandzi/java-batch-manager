package com.javabatchmanager.web;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.web.GlobalJobVar.JobType;

@Controller
@RequestMapping("/job-type")
public class SelectJobController extends AbstractController{
	
	
	private static final String jobLauncherUrl = "/launchable-jobs";
	private static final String pastJobsUrl = "/past-jobs";
	private static final String runningJobsUrl = "/running-jobs";
	
	@RequestMapping(method = RequestMethod.POST)
	public String setTypeToJSR352(@ModelAttribute("globVar") GlobalJobVar globVar, ModelMap model,HttpServletRequest request){
		String refererUrl = request.getHeader("referer");
		if(globVar.getJobType() == GlobalJobVar.JobType.JSR_352_BATCH){
			activeJobService=JobType.JSR_352_BATCH;
			setJobService(jobServiceJSR);
		}
		if(globVar.getJobType() == GlobalJobVar.JobType.SPRING_BATCH){
			activeJobService=JobType.SPRING_BATCH;
			setJobService(jobServiceSpring);
		}
		
		if(refererUrl.contains(pastJobsUrl)){
			return "redirect:"+pastJobsUrl;
		}
		if(refererUrl.contains(runningJobsUrl)){
			return "redirect:"+runningJobsUrl;
		}
		return "redirect:"+jobLauncherUrl;
		
	}

}
