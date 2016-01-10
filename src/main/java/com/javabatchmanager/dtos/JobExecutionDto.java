package com.javabatchmanager.dtos;

import java.util.Date;
import java.util.List;

/**
 * Our object representing JobExecution.
 */
public class JobExecutionDto {
	private long jobExecutionId;
	private String jobName;
	private String parameters;
	private Date createTime;
	private Date startTime;
	private Date finishedTime;
	private String status;
	private String exitDescription;
	private boolean stop = false;
	private boolean isRestartable;
	private String jobType;
	private String idAndType;
	private List<StepExecutionDto> stepExecutionsDto;
	
	public List<StepExecutionDto> getStepExecutionsDto() {
		return stepExecutionsDto;
	}
	public void setStepExecutionsDto(List<StepExecutionDto> stepExecutionsDto) {
		this.stepExecutionsDto = stepExecutionsDto;
	}
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
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getIdAndType() {
		return idAndType;
	}
	public void setIdAndType(String idAndType) {
		this.idAndType = idAndType;
	}
	public Date getFinishedTime() {
		return finishedTime;
	}
	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}
	public String getExitDescription() {
		return exitDescription;
	}
	public void setExitDescription(String exitDescription) {
		this.exitDescription = exitDescription;
	}
}
