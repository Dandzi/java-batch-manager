package com.javabatchmanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Set;


import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.error.ExceptionCause;

@ContextConfiguration(locations = { "classpath:context/test-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringBatchJobServiceTest {

	static {
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.INFO);
		rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
	}

	@Autowired
	@Qualifier("SpringBatch")
	protected JobService jobService;

	@Autowired
	protected JobOperator jobOperator;

	@Autowired
	protected JobExplorer jobExplorer;

	@Test
	public void jobServiceNotNull() {
		assertThat(jobService).isNotNull();
		assertThat(jobOperator).isNotNull();
		assertThat(jobExplorer).isNotNull();
	}
	//start
	@Test
	public void launchJobTest() throws BaseBatchException {
		containsExampleJobTest();
		JobExecutionDto jobExecDto = jobService.start("springtestjob", null);
		assertThat(jobExecDto).isNotNull();
		Long id = jobExecDto.getJobExecutionId();
		JobExecution jobExec = jobExplorer.getJobExecution(id);
		assertThat(jobExec).isNotNull();
		assertThat(jobExec.getId()).isEqualTo(id);
	}

	@Test
	public void launchJobTwiceTest() throws BaseBatchException {
		JobExecutionDto jobExecDto = jobService.start("springtestjob", "test3:test3");
		try {
			JobExecutionDto jobExecDto2 = jobService.start("springtestjob", "test3:test3");
		} catch (BaseBatchException e) {
			assertThat(e.causeEnum).isEqualTo(ExceptionCause.JOB_INSTANCE_ALREADY_EXISTS);
		}
	}
	
	@Test
	public void launchingNonExistentJobTest(){
		try {
			JobExecutionDto jobExecDto = jobService.start("IDontExistJob", null);
			fail();
		} catch (BaseBatchException e) {
			assertThat(e.causeEnum).isEqualTo(ExceptionCause.NO_SUCH_JOB);
		}
		
	}
	
	//stop

	@Test
	public void stopJobTest() throws NoSuchJobException, JobInstanceAlreadyExistsException,
			JobParametersInvalidException, BaseBatchException {
		JobExecutionDto jobExec = jobService.start("springtestjob", "test2:test2");
		JobExecutionDto jobExecDto = jobService.stop(jobExec.getJobExecutionId());
		assertThat(jobExec.getJobExecutionId()).isEqualTo(jobExecDto.getJobExecutionId());
	}
	
	@Test
	public void stoppingNonExistentJobTest(){
		try {
			JobExecutionDto jobExecDto = jobService.stop(-1L);
			fail();
		} catch (BaseBatchException e) {
			assertThat(e.causeEnum).isEqualTo(ExceptionCause.NO_SUCH_JOB_EXECUTION);
		}		
	}
	
	@Test
	public void stoppingNotRunningJobtest(){
		try{
			JobExecutionDto jobExec = jobService.start("springtestjob", "key:value8");
			jobExec = jobService.stop(jobExec.getJobExecutionId());
			jobExec = jobService.stop(jobExec.getJobExecutionId());
			if(!"STOPPING".equals(jobExec.getStatus())){
				fail();			
			}
		}catch (BaseBatchException e) {
			assertThat(e.causeEnum).isEqualTo(ExceptionCause.JOB_EXECUTION_NOT_RUNNING);
		}
	}
	
	@Test
	public void stoppingCompletedJobTest(){
		try{
			JobExecutionDto jobExec = jobService.start("springtestjob", "key:value1");
			while(!"COMPLETED".equals(jobExec.getStatus())
					&&!"FAILED".equals(jobExec.getStatus())){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.err.println(e.getMessage());
				}
				jobExec = jobService.getJobExecutionById(jobExec.getJobExecutionId());
			}
			jobExec = jobService.stop(jobExec.getJobExecutionId());
			fail();
		}catch (BaseBatchException e) {
			assertThat(e.causeEnum).isEqualTo(ExceptionCause.JOB_EXECUTION_NOT_RUNNING);
		}
	}
	
	//restart
	@Test
	public void restartCompletedJobTest(){
		try{
			JobExecutionDto jobExec = jobService.start("springtestjob", "key:test5");
			while(!"COMPLETED".equals(jobExec.getStatus())
					&&!"FAILED".equals(jobExec.getStatus())){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.err.println(e.getMessage());
				}
				jobExec = jobService.getJobExecutionById(jobExec.getJobExecutionId());
			}
			jobExec = jobService.restart(jobExec.getJobExecutionId());
		} catch(BaseBatchException ex){
			System.err.println(ex.getMessage());
			fail();
		}
	
	}
	
	@Test
	public void restartJobAfterItReallyStopTest(){
		try{
			JobExecutionDto jobExec = jobService.start("springtestjob", "key:value3");
			jobExec = jobService.stop(jobExec.getJobExecutionId());
			while(!"STOPPED".equals(jobExec.getStatus())){
				Thread.sleep(500);
				jobExec = jobService.getJobExecutionById(jobExec.getJobExecutionId());
				if("COMPLETED".equals(jobExec.getStatus())
						&&!"FAILED".equals(jobExec.getStatus())){
					break;
				}
			}
			jobExec = jobService.restart(jobExec.getJobExecutionId());
		} catch(BaseBatchException ex){
			System.err.println(ex.getMessage());
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void restartJobRightAfterStartTest(){
		
	}

	@Test
	public void restartNonExistentExecTest(){
		try{
			JobExecutionDto jobExec = jobService.restart(-1L);
			fail();			
		} catch(BaseBatchException ex){
			String cause = ex.causeEnum.name();
			if(!cause.equals("NO_SUCH_JOB_EXECUTION")){
				Assert.fail("Wrong exception thrown");
			}			
		}
	}
	
	// executions
	@Test
	public void getJobExecutionByIdTest() throws NoSuchJobException, JobInstanceAlreadyExistsException,
			JobParametersInvalidException, BaseBatchException {
		JobExecutionDto jobExec = jobService.start("springtestjob", "test:test");
		JobExecutionDto jobExecDto = jobService.getJobExecutionById(jobExec.getJobExecutionId());
		assertThat(jobExec.getJobExecutionId()).isEqualTo(jobExecDto.getJobExecutionId());

	}

	@Test
	public void getJobExecutionsByInstanceTest() {
		try {
			JobExecutionDto jobExec = jobService.start("springtestjob", "test4:test4");
			List<JobInstanceDto> jobInstances = jobService.getJobInstances("springtestjob", 0, 500);
			List<JobExecutionDto> jobExecs = jobService.getJobExecutions(jobInstances.get(0));
			assertThat(jobExecs).isNotEmpty();
		} catch (BaseBatchException e) {
			fail();
		}
	}
	
	@Test
	public void getJobExecutionsByNullTest(){
		try{
			List<JobExecutionDto> execOfInstanceList = jobService.getJobExecutions(null);
			assertThat(execOfInstanceList).isEmpty();
		}catch(BaseBatchException e){
			fail();
		}
	}
	
	@Test
	public void getRunningJobExecsNullTest(){
		try{
			Set<JobExecutionDto> runningExec = jobService.getRunningJobExecutions(null);
			assertThat(runningExec).isEmpty();
		}catch(BaseBatchException e){
			fail();
		}
	}

	
	//instances
	@Test
	public void getJobInstancesOfNonExistentJobTest(){
		try{
			List<JobInstanceDto> instDtos = jobService.getJobInstances("IDontExistJob", 1, 0);
			assertThat(instDtos).isEmpty();
		} catch(BaseBatchException ex){
			
		}	
	}
	
	@Test
	public void getJobInstancesWithNegativeBoundaries(){
		try{
			List<JobInstanceDto> instDtos = jobService.getJobInstances("IDontExistJob", -10, -5);
			assertThat(instDtos).isEmpty();
		} catch(BaseBatchException ex){
			fail();
		}	
	}
	
	@Test
	public void getJobInstancesTest(){
		try{
			JobExecutionDto jobExec = jobService.start("springtestjob", "key:value");
			List<JobInstanceDto> instanceDtos = jobService.getJobInstances("springtestjob", 0, 100);
			assertThat(instanceDtos).isNotEmpty();
		} catch(BaseBatchException ex){
			fail();
		}
	}
	
	@Test
	public void containsExampleJobTest() {
		Set<String> jobNames = jobService.getJobNames();
		assertThat(jobNames).isNotEmpty();
		if (!jobNames.contains("springtestjob")) {
			fail();
		}
	}

}
