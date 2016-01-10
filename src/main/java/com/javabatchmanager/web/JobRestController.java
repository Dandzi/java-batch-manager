package com.javabatchmanager.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.error.EmptyOrNullValueException;
import com.javabatchmanager.error.RestException;
import com.javabatchmanager.error.RestIllegalRequestException;
import com.javabatchmanager.service.JobService;
import com.javabatchmanager.watchers.Listener;

@RestController
@RequestMapping(value = "/rest")
public class JobRestController {

	@Autowired
	private JobService jobService;


	/*
	 * launch
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/launch")
	public JobExecutionDto launch(@RequestBody JobInstanceDto jld) throws RestException {
		if (jld == null) {
			throw new EmptyOrNullValueException("Null object.", 
												new NullPointerException("JobInstanceDto object is null"), 
												new String[] {}, "job.rest.object.null");
		}
		JobExecutionDto jobExec = null;
		try {
			jobExec = jobService.start(jld.getJobName(), jld.getParameters());
		} catch (BaseBatchException e) {
			throw new RestIllegalRequestException(e.getMessage(), 
													e,
													new String[] { jld.getJobName(), jld.getParameters() },
													e.getCauseEnum().getErrCode());
		}
		return jobExec;
	}
/*
 * TODO
 * 	  |
 * 	  |
 *  \ | /
 *   \|/
 */
	
	/*
	 * stop 	//TODO jobExecution exception - basebatch exc
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/stop/{id}")
	public JobExecutionDto stop(@PathVariable Long id) throws RestException, BaseBatchException {
		if (id == null) {
			throw new RestIllegalRequestException(null,
													new NullPointerException("Parameter id is null"),
													new String[] {}, 
													"job.rest.id.null");
		}
		try {
			jobService.stop(id);
		} catch (BaseBatchException e) {
			throw new RestIllegalRequestException(e.getMessage(), 
					e,
					new String[] {},
					e.getCauseEnum().getErrCode());
		}
		JobExecutionDto jobExec = jobService.getJobExecutionById(id);
		return jobExec;
	}

	
	/*
	 * restart
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/restart/{id}")
	public JobExecution restart(@PathVariable Long id) throws RestException {
		try {
			jobService.restart(id);
		} catch (BaseBatchException e) {
			throw new RestIllegalRequestException(e.getMessage(), 
					e,
					new String[] {},
					e.getCauseEnum().getErrCode());
		}
		return null;
	}

	//TODO jobExecution exception - basebatch exc
	@RequestMapping(method = RequestMethod.GET, value = "/execution/{id}")
	public JobExecutionDto getJobExecById(@PathVariable Long id)
			throws RestException, BaseBatchException {
		if (id == null) {
			throw new RestIllegalRequestException(null,
					new NullPointerException("Parameter id is null"),
					new String[] {}, "job.rest.id.null");
		}
		JobExecutionDto jobExec = jobService.getJobExecutionById(id);
		if (jobExec == null) {
			throw new EmptyOrNullValueException("JobExecution is null",
					new NullPointerException("Execution with id " + id
							+ " does not exist."),
					new String[] { id.toString() }, "job.rest.execution.null");
		}
		return jobExec;
	}

	// TODO
	@RequestMapping(method = RequestMethod.GET, value = "/running/all")
	public List<JobExecutionDto> getRunningJobExecutions() throws RestException {
		List<JobExecutionDto> jobExecs = new ArrayList<JobExecutionDto>();
		try {
			jobExecs = jobService.getAllRunningJobExecutions();
		} catch (BaseBatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (jobExecs.isEmpty()) {
			throw new EmptyOrNullValueException(
					"Empty List of JobExecutionDto", null,
					new String[] { jobExecs.toString() },
					"job.rest.execution.no.running");
		}
		return jobExecs;
	}
}
