package com.javabatchmanager.service;

import java.util.List;
import java.util.Set;

import javax.batch.operations.JobExecutionNotMostRecentException;
import javax.batch.operations.JobExecutionNotRunningException;
import javax.batch.operations.JobRestartException;
import javax.batch.operations.JobSecurityException;
import javax.batch.operations.NoSuchJobExecutionException;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.error.ExceptionCause;

/**
 * Core interface for launching, stopping, restarting jobs, and getting informations about them.
 * @author daniel
 *
 */
public interface JobService {
	
	
	/**
	 * Method for launchig the job.
	 * @param jobName we try to start job with this name 
	 * @param jobParams the paramaters with which the job will be started
	 * @return JobExecutionDto object with information about launched jobExecution.
	 * @throws BaseBatchException with cause NoSuchJob when Job with given name cannot be found, or for launching.
	 * 		   <br/>
	 * 		   BaseBatchException with cause JobParametersInvalid when job parameters are not valid.
	 *  	   <br/>
	 * 		   BaseBatchException with cause JobInstanceAlreadyExists when jobInstance with given name and params already exists.
	 * 		   <br/>
	 * 		   When UnexpectedJobExecutionException occurs method will print its stacktrace
	 * 	<br/> JOB_SECURITY - JSR352 when JobSecurityException happens
	 */
	public JobExecutionDto start(String jobName, String jobParams) throws BaseBatchException;
	
	
	
	/**
	 * This method restarts execution by given id.
	 * @param executionId id of execution to be restarted
	 * @return JobExecutionDto object
	 * @throws BaseBatchException throw when these causes happens:
	 * <br/>JOB_RESTART <br/>- Spring An exception indicating an illegal attempt to restart a job.
	 * 				<br/>- JSR352 An exception indicating an illegal attempt to restart a job.
	 * 
	 * <br/>JOB_INSTANCE_ALREADY_COMPLETE-Spring An exception indicating an illegal attempt to restart a job that was already completed successfully.
	 * 
	 * <br/>NO_SUCH_JOB-Spring Checked exception to indicate that a required Job is not available.
	 * 
	 * <br/>NO_SUCH_JOB_EXECUTION-Spring Checked exception to indicate that a required JobExecution is not available.
	 * 
	 * <br/>JOB_PARAMETERS_INVALID-Spring Exception for Job to signal that some JobParameters are invalid.
	 *
	 * <br/>JOB_EXECUTION_ALREADY_COMPLETE-JSR352 When JobExecution already ended successfully
	 * 
	 * <br/> JOB_EXECUTION_NOT_MOST_RECENT-JSR352 not most recent execution is trying to be restarted.
	 * 
	 * <br/> JOB_SECURITY - JSR352 when JobSecurityException happens
	 */
	public JobExecutionDto restart(Long executionId) throws BaseBatchException;


	
	
	/**
	 * Stop running execution.
	 * 
	 * @param executionId this execution will be stopped
	 * @throws BaseBatchException throw when these causes happens:<br/>
	 * NO_SUCH_JOB_EXECUTION <br/>
	 * 						- JSR352 when there is no execution with given id <br/>
	 * 						- Spring when there is no execution with given id<br/>
	 * JOB_EXECUTION_NOT_RUNNING  <br/>
	 * 						- JSR352 when given execution is not running <br/>
	 * 						- Spring when given execution is not running <br/>
	 * JOB_SECURITY - JSR352 when JobSecurityException happens <br/>
	 * @return JobExecutionDto object 
	 */
	public JobExecutionDto stop(Long executionId) throws BaseBatchException;


	/**
	 * Method returns set of launchable jobNames known to runtime.
	 * 
	 * @return Set<String> jobNames - set of jobnames which can be launched.
	 */
	public Set<String> getJobNames() ;

	/**
	 * This method return list of JobInstanceDto.
	 * 
	 * @param jobName search jobInstances by given name
	 * @param start id from which to start searching in jobInstances
	 * @param count maximum count of jobInstances
	 * @return List of jobInstanceDto, if there are no JobInstances return empty List 
	 * @throws BaseBatchException JSR352 when jobSecurity exception occurs
	 */

	public List<JobInstanceDto> getJobInstances(String jobName, int start, int count) throws BaseBatchException;
	
	/**
	 * Method returns all job executions of given jobInstance, if there is none, returns empty list
	 * @param jobInstance of which jobExecutions are returned
	 * @return List of jobExecutionDto, if there is none return empty list.
	 * @throws BaseBatchException JSR352 when jobSecurity exception occurs
	 */

	public List<JobExecutionDto> getJobExecutions(JobInstanceDto jobInstance) throws BaseBatchException;


	/**
	 * Returns JobExecutionDto information object by id of JobExecution.
	 * @param id of jobExecution, which we want to return
	 * @return if jobExecution doesnt exist (is null) returns null, else return JobExecutionDto object
	 * @throws BaseBatchException JSR352 when jobSecurity exception occurs
	 * cause: NO_SUCH_JOB_EXECUTION when no NoSuchJobExecutionException | NullPointerException occurs.
	 */
	public JobExecutionDto getJobExecutionById(Long id) throws BaseBatchException;

	
	/**
	 * Returns set of running job executions of given job name.
	 * When NoSuchJobException occurs because of no job execution of given name was ever launched, returns empty HashSet.
	 * NoSuchJobException is defined for both Spring Batch and JSR352
	 * @param jobName returns executions by this name
	 * @return Set of jobExecutionDto of given name
	 * @throws BaseBatchException JSR352 when jobSecurity exception occurs
	 */
	public Set<JobExecutionDto> getRunningJobExecutions(String jobName) throws BaseBatchException;

	/**
	 * Finds all running job executions. When no executions are running returns empty list.
	 * 
	 * @return List of all running jobExecutions.
	 * @throws BaseBatchException JSR352 when jobSecurity exception occurs
	 */
	public List<JobExecutionDto> getAllRunningJobExecutions() throws BaseBatchException;

	
}
