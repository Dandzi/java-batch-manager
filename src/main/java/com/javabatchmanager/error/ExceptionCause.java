package com.javabatchmanager.error;

public enum ExceptionCause {
		//JSR352
		NO_SUCH_JOB("no.such.job"), //common
		JOB_EXECUTION_ALREADY_COMPLETE("job.execution.already.complete"),
		JOB_EXECUTION_NOT_MOST_RECENT("job.execution.not.most.recent"),
		JOB_EXECUTION_NOT_RUNNING("job.execution.not.running"), //common
		JOB_EXECUTION_IS_RUNNING("job.execution.is.runnig"),
		JOB_RESTART("job.restart"),//common
		JOB_SECURITY("job.security"),
		JOB_START("job.start"),
		NO_SUCH_JOB_EXECUTION("no.such.job.execution"),//common
		NO_SUCH_JOB_INSTANCE("no.such.job.instance"),//common
		
		
		//Spring
		DUPLICATE_JOB("duplicate.job"),
		JOB_EXECUTION_ALREADY_RUNNING("job.execution.already.running"),
		JOB_EXECUTION_NOT_FAILED("job.execution.not.failed"),
		JOB_EXECUTION_NOT_STOPPED("job.execution.not.stopped"),
		JOB_INSTANCE_ALREADY_EXISTS("job.instance.already.exists"),
		JOB_INSTANCE_ALREADY_COMPLETE("job.instance.already.complete"),
		JOB_INTERRUPTED("job.interrupted"),
		JOB_PARAMETERS_INVALID("job.parameters.invalid"),
		JOB_PARAMETERS_NOT_FOUND("job.parameters.not.found"),
		JOB_EXECUTION("job.execution");
		private final String errCode;
		
		ExceptionCause(String errCode){
			this.errCode=errCode;
		}
		
		public String getErrCode() {
			return this.errCode;
		}
}