package com.javabatchmanager.dtos;

import java.io.Serializable;
import java.util.Date;
/*
 * Dto object for info about steps. 
 */
public class StepExecutionDto {
    private long id;

    private String stepName;

    private Serializable persistentUserData;

    private Serializable readerCheckpointInfo;

    private Serializable writerCheckpointInfo;

    private String exitDescription;
    
    private Date startTime;
    
	private Date endTime;
	
    private String exitStatus;
    
    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public Serializable getPersistentUserData() {
		return persistentUserData;
	}
	public void setPersistentUserData(Serializable persistentUserData) {
		this.persistentUserData = persistentUserData;
	}
	public Serializable getReaderCheckpointInfo() {
		return readerCheckpointInfo;
	}
	public void setReaderCheckpointInfo(Serializable readerCheckpointInfo) {
		this.readerCheckpointInfo = readerCheckpointInfo;
	}
	public Serializable getWriterCheckpointInfo() {
		return writerCheckpointInfo;
	}
	public void setWriterCheckpointInfo(Serializable writerCheckpointInfo) {
		this.writerCheckpointInfo = writerCheckpointInfo;
	}

	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getExitStatus() {
		return exitStatus;
	}
	public void setExitStatus(String exitStatus) {
		this.exitStatus = exitStatus;
	}
	public String getExitDescription() {
		return exitDescription;
	}
	public void setExitDescription(String exitDescription) {
		this.exitDescription = exitDescription;
	}




}


