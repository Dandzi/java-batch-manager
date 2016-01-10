package com.javabatchmanager.dtos;

/*
 * Object using by JobRestController to determine id of job execution, jobName and JobType
 */
public class JobWrapperForRest {
	private String jobType;
	private String jobName;
	private Long idOfJob;
	
	public Long getIdOfJob() {
		return idOfJob;
	}

	public void setIdOfJob(Long idOfJob) {
		this.idOfJob = idOfJob;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
}
