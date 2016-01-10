package com.javabatchmanager.web;

import com.javabatchmanager.error.BaseBatchException;
import org.springframework.validation.BindingResult;
/*
 * NO_SUCH_JOB, //common
		JOB_EXECUTION_ALREADY_COMPLETE,
		JOB_EXECUTION_NOT_MOST_RECENT,
		JOB_EXECUTION_NOT_RUNNING, //common
		JOB_EXECUTION_IS_RUNNING,
		JOB_RESTART,//common
		JOB_SECURITY,
		JOB_START,
		NO_SUCH_JOB_EXECUTION,//common
		NO_SUCH_JOB_INSTANCE
		
				//Spring
		DUPLICATE_JOB,
		JOB_EXECUTION_ALREADY_RUNNING,
		JOB_EXECUTION_NOT_FAILED,
		JOB_EXECUTION_NOT_STOPPED,
		JOB_INSTANCE_ALREADY_EXISTS,
		JOB_INSTANCE_ALREADY_COMPLETE,
		JOB_INTERRUPTED,
		JOB_PARAMETERS_INVALID,
		JOB_PARAMETERS_NOT_FOUND;
		
		/*} catch (NoSuchJobException e) {
			errors.rejectValue("jobName","job.start.error.not.exist", new Object[] {jid.getJobName()},null);
		} catch (JobExecutionAlreadyRunningException e) {
			errors.rejectValue("jobName","job.start.error.running", new Object[] {jid.getJobName(), jid.getParameters()},null);
		} catch (JobRestartException e) {
			errors.rejectValue("jobName","job.start.error.restart", new Object[] {jid.getJobName()},null);
		} catch (JobInstanceAlreadyCompleteException e) {
			errors.rejectValue("jobName","job.start.error.instance.complete", new Object[] {jid.getJobName(),jid.getParameters()},null);
		} catch (JobParametersInvalidException e) {
			errors.rejectValue("jobName","job.start.error.wrong.params", new Object[] {jid.getParameters()},null);
		}*/
		
 

public class ControllerExceptionHandler {

	public static void handleException(BaseBatchException e, BindingResult error, Object[] args){
		
		switch(e.getCauseEnum()){
			case NO_SUCH_JOB:
				error.rejectValue("jobName", e.getCauseEnum().getErrCode(),args,null);
			case JOB_EXECUTION_ALREADY_COMPLETE:
				error.rejectValue("jobName",  e.getCauseEnum().getErrCode(),args,null);
			case JOB_RESTART:
				error.rejectValue("jobName",  e.getCauseEnum().getErrCode(),args,null);			
			case JOB_INSTANCE_ALREADY_COMPLETE:
				error.rejectValue("jobName",  e.getCauseEnum().getErrCode(),args,null);
			case JOB_PARAMETERS_INVALID:
				error.rejectValue("jobName",  e.getCauseEnum().getErrCode(),args,null);
			case JOB_START:
				error.rejectValue("jobName", e.getCauseEnum().getErrCode(),args,null);
		}
	}
}
