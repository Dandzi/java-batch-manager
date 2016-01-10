package com.javabatchmanager.dtos;

import java.util.Date;

import org.springframework.batch.core.BatchStatus;

public class JobExecutionDto {
	private long jobExecutionId;
	private String jobName;
	private String parameters;
	private Date createTime;
	private Date startTime;
	private String status;
	private boolean stop = false;
	private boolean isRestartable;
	
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	
	public boolean isStop() {
		return stop;
	}
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	public long getJobExecutionId() {
		return jobExecutionId;
	}
	public void setJobExecutionId(long jobExecutionId) {
		this.jobExecutionId = jobExecutionId;
	}
	public boolean getIsRestartable() {
		return isRestartable;
	}
	public void setRestartable(boolean isRestartable) {
		this.isRestartable = isRestartable;
	}
}
