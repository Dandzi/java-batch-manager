package com.javabatchmanager.web;

import org.springframework.stereotype.Component;

@Component
public class GlobalJobVar {
	public JobType jobType;
	
	public enum JobType {
		JSR_352_BATCH("JSR352Batch"),
		SPRING_BATCH("SpringBatch");
		
		private final String stringTypeRepr;

		private JobType(String stringTypeRepr) {
			this.stringTypeRepr = stringTypeRepr;
		}

		public String getStringTypeRepr() {
			return stringTypeRepr;
		}
			
	}

	public JobType getJobType() {
		return jobType;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	
	
}
