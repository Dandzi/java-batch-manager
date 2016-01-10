package com.javabatchmanager.dtos;


import org.junit.Test;
import java.util.Date;
import junit.framework.TestCase;

public class DtoObjectTest extends TestCase {


    @Test
    public void testJobExecutionDto() {
    	JobExecutionDto jobExecutionDto = new JobExecutionDto();
    	jobExecutionDto.setJobExecutionId(0);
    	Date createTime = new Date();
    	jobExecutionDto.setCreateTime(createTime);
    	String name = "jobName";
    	jobExecutionDto.setJobName(name);
    	assertEquals("JobExecutionDto name should be jobName", "jobName", jobExecutionDto.getJobName());
    	assertEquals("JobExecutionDto id should be 0", 0, jobExecutionDto.getJobExecutionId());
    	assertEquals("JobExecutionDto date should be"+createTime.getTime(), createTime, jobExecutionDto.getCreateTime());
    	
    }
    
    @Test
    public void testJobInstanceDto(){
    	JobInstanceDto jobInstanceDto = new JobInstanceDto();
    	jobInstanceDto.setJobInstanceId(0L);
    	jobInstanceDto.setJobName("jobName");
    	jobInstanceDto.setJobType("jsr");
    	assertEquals("JobInstanceDto name should be jobName", "jobName", jobInstanceDto.getJobName());
    	assertEquals("JobInstanceDto type should be jsr","jsr", jobInstanceDto.getJobType());
    	assertEquals("JobInstanceDto id should be 0", Long.valueOf(0), jobInstanceDto.getJobInstanceId());

    }
}
