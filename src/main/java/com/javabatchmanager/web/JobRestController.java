package com.javabatchmanager.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstRest;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.dtos.JobWrapperForRest;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.error.EmptyOrNullValueException;
import com.javabatchmanager.error.RestException;
import com.javabatchmanager.error.RestIllegalRequestException;
import com.javabatchmanager.service.JobService;

@RestController
@RequestMapping(value = "/rest")
public class JobRestController {
	
	@Autowired
	@Qualifier("JSR352Batch")
	protected JobService jobServiceJSR;
	
	@Autowired
	@Qualifier("SpringBatch")
	protected JobService jobServiceSpring;	


	/*
	 * launch
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/launch")
	public JobExecutionDto launch(@RequestBody JobInstRest jobInstDto) throws RestException {
		if (jobInstDto == null || jobInstDto.getJobName() == null)  {
			throw new EmptyOrNullValueException("Null object.", 
												new NullPointerException("JobInstanceDto object is null"), 
												new String[] {}, "job.rest.object.null");
		}
		JobExecutionDto jobExec = null;
		JobService jobService = getJobServiceByType(jobInstDto.getJobType());
		
		try {
			jobExec = jobService.start(jobInstDto.getJobName(), jobInstDto.getParameters());
		} catch (BaseBatchException e) {
			e.printStackTrace();
			throw new RestIllegalRequestException(e.getMessage(), 
													e,
													new String[] { jobInstDto.getJobName(), jobInstDto.getParameters() },
													e.getCauseEnum().getErrCode());
			
		}
		return jobExec;
	}

	
	/*
	 * stop 	
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/stop")
	public JobExecutionDto stop(@RequestBody JobWrapperForRest jobObj) throws RestException, BaseBatchException {
		try{
			checkJobObj(jobObj);		
		} catch(RestIllegalRequestException e){			
			throw e;
		}
		
		Long id = jobObj.getIdOfJob();
		if (id == null) {
			throw new RestIllegalRequestException(null,
													new NullPointerException("Parameter id is null"),
													new String[] {}, 
													"job.rest.id.null");
		}
		if (jobObj.getJobType() == null) {
			throw new RestIllegalRequestException("JobService type is null");
		}
		JobService jobService = getJobServiceByType(jobObj.getJobType());
		
		try {
			jobService.stop(id);
		} catch (BaseBatchException e) {
			throw new RestIllegalRequestException(e.getMessage(), 
					e,
					new String[] {jobObj.getIdOfJob().toString()},
					e.getCauseEnum().getErrCode());
		}
		JobExecutionDto jobExec = jobService.getJobExecutionById(id);
		return jobExec;
	}

	
	/*
	 * restart
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/restart")
	public JobExecutionDto restart(@RequestBody JobWrapperForRest jobObj) throws RestException {

		try{
			checkJobObj(jobObj);		
		} catch(RestIllegalRequestException e){
			throw e;
		}
		Long id = jobObj.getIdOfJob();
		if (id == null) {
			throw new RestIllegalRequestException(null,
					new NullPointerException("Parameter id is null"),
					new String[] {}, "job.rest.id.null");
		}
		JobService jobService = getJobServiceByType(jobObj.getJobType());
		try {
			JobExecutionDto jobExec =  jobService.restart(id);
			return jobExec;
		} catch (BaseBatchException e) {
			throw new RestIllegalRequestException(e.getMessage(), 
					e,
					new String[] {id.toString()},
					e.getCauseEnum().getErrCode());
		}
	}

	
	/*
	 * jobNames
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "/jobnames")
	public Set<String>  getJobNames(@RequestBody JobWrapperForRest jobObj)
			throws RestException{

		try{
			checkJobObj(jobObj);		
		} catch(RestIllegalRequestException e){
			throw e;
		}
		
		JobService jobService = getJobServiceByType(jobObj.getJobType());
		Set<String> jobNames = jobService.getJobNames();
		if (jobNames == null) {
			throw new EmptyOrNullValueException("No jobs available",
					null,
					new String[] {}, "no.jobs.available");
		}
		return jobNames;
	}
	
	/*
	 * get execution by id
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/execution")
	public JobExecutionDto getJobExecById(@RequestBody JobWrapperForRest jobObj)
			throws RestException, BaseBatchException {

		try{
			checkJobObj(jobObj);		
		} catch(RestIllegalRequestException e){
			throw e;
		}
		
		Long id = jobObj.getIdOfJob();
		if (id == null) {
			throw new RestIllegalRequestException(null,
					new NullPointerException("Parameter id is null"),
					new String[] {}, "job.rest.id.null");
		}
		if (jobObj.getJobType() == null) {
			throw new RestIllegalRequestException("JobService type is null");
		}
		JobService jobService = getJobServiceByType(jobObj.getJobType());
		JobExecutionDto jobExec=null;
		try{
			jobExec = jobService.getJobExecutionById(id);
		}catch(BaseBatchException ex){
			throw new RestException(ex.getMessage(), ex, new String[]{jobObj.getIdOfJob().toString()}, ex.causeEnum.getErrCode());
		}
		if (jobExec == null) {
			throw new EmptyOrNullValueException("JobExecution is null",
					new NullPointerException("Execution with id " + id
							+ " does not exist."),
					new String[] { id.toString() }, "job.rest.execution.null");
		}
		return jobExec;
	}

	/*
	 * running all
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/running/all")
	public List<JobExecutionDto> getRunningJobExecutions(@RequestBody JobWrapperForRest jobObj) throws RestException {
		List<JobExecutionDto> jobExecs = new ArrayList<JobExecutionDto>();

		try{
			checkJobObj(jobObj);		
		} catch(RestIllegalRequestException e){
			throw e;
		}
		
		JobService jobService = getJobServiceByType(jobObj.getJobType());
		try {
			jobExecs = jobService.getAllRunningJobExecutions();
		} catch (BaseBatchException e) {
			e.printStackTrace();
		}
		if (jobExecs.isEmpty()) {
			throw new EmptyOrNullValueException(
					"Empty List of JobExecutionDto", null,
					new String[] { jobExecs.toString() },
					"job.rest.no.running.executions");
		}
		return jobExecs;
	}
	
	
	/*
	 * running of given jobName
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "/running/name")
	public Set<JobExecutionDto> getRunningJobExecutionsByJobName(@RequestBody JobWrapperForRest jobObj) throws RestException {
		Set<JobExecutionDto> jobExecs = new HashSet<JobExecutionDto>();
		
		try{
			checkJobObj(jobObj);		
		} catch(RestIllegalRequestException e){
			throw e;
		}
		
		if(jobObj.getJobName() == null) {
			throw new RestIllegalRequestException("Param jobName is null");
		}
		String jobName=jobObj.getJobName();
		JobService jobService = getJobServiceByType(jobObj.getJobType());
		try {
			jobExecs = jobService.getRunningJobExecutions(jobName);
		} catch (BaseBatchException e) {
			e.printStackTrace();
		}
		if (jobExecs.isEmpty()) {
			throw new EmptyOrNullValueException(
					"Empty List of JobExecutionDto. There are probably no execution of given name.", null,
					new String[] { jobExecs.toString() },
					"job.rest.no.running.executions");
		}
		return jobExecs;
	}
	
	//instances
	@RequestMapping(method = RequestMethod.GET, value = "/instances")
	public List<JobInstanceDto> getJobInstancesDto(@RequestBody JobInstRest jobInstDto) throws RestException{
		if (jobInstDto == null || jobInstDto.getJobName() == null)  {
			throw new EmptyOrNullValueException("Null object.", 
												new NullPointerException("JobInstanceDto object is null"), 
												new String[] {}, "job.rest.object.null");
		}
		List<JobInstanceDto> jobInstDtoList = new ArrayList<>();
		JobService jobService = getJobServiceByType(jobInstDto.getJobType());
		try {
			jobInstDtoList = jobService.getJobInstances(jobInstDto.getJobName(), jobInstDto.getStart(), jobInstDto.getCount());
		} catch (BaseBatchException e) {
			e.printStackTrace();
		}
		if (jobInstDtoList.isEmpty()) {
			throw new EmptyOrNullValueException(
					"Empty List of JobInstanceDto. There are probably no instances with given job name.", null,
					new String[] { jobInstDtoList.toString() },
					"job.rest.no.instances");
		}
		return jobInstDtoList;
		
	}
	
	
	private boolean checkJobObj(JobWrapperForRest jobObj) throws RestIllegalRequestException{
		if(jobObj == null) {
			throw new RestIllegalRequestException(" Objet JobWrapperForRest is null");
		}
		if (jobObj.getJobType() == null) {
			throw new RestIllegalRequestException("JobService type is null");
		}
		return true;
	}
	
	
	private JobService getJobServiceByType(String type) throws RestException{
		if("JSR352".equals(type)){
			return jobServiceJSR;
		}
		if("SPRINGBATCH".equals(type)){
			return jobServiceSpring;
		}
		throw new RestException("Illegal type of JobService, must be JSR352 or SPRINGBATCH","job.rest.jobtype.wrong");
	}
}
