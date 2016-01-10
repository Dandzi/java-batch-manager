package com.javabatchmanager.dtos;


public class JobInstanceWrapper {
	private javax.batch.runtime.JobInstance jsrJobInst;
	private org.springframework.batch.core.JobInstance springJobInst;
	private JobInstanceDto jobInstDto;
	
	public org.springframework.batch.core.JobInstance getSpringJobInst() {
		return springJobInst;
	}
	public void setSpringJobInst(org.springframework.batch.core.JobInstance springJobInst) {
		this.springJobInst = springJobInst;
	}
	public javax.batch.runtime.JobInstance getJsrJobInst() {
		return jsrJobInst;
	}
	public void setJsrJobInst(javax.batch.runtime.JobInstance jsrJobInst) {
		this.jsrJobInst = jsrJobInst;
	}
	public JobInstanceDto getJobInstDto() {
		return jobInstDto;
	}
	public void setJobInstDto(JobInstanceDto jobInstDto) {
		this.jobInstDto = jobInstDto;
	}
		
}
