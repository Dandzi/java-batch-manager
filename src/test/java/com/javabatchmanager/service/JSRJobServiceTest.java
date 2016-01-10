package com.javabatchmanager.service;


import static org.junit.Assert.fail;

import java.util.List;
import java.util.Set;

import org.jberet.operations.JobOperatorImpl;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.support.PropertiesConverter;
import org.springframework.util.PropertiesPersister;

import com.javabatchmanager.dtos.JobExecutionDto;
import com.javabatchmanager.dtos.JobInstanceDto;
import com.javabatchmanager.error.BaseBatchException;
import com.javabatchmanager.example.jsr.SimpleItemReader;
import com.javabatchmanager.service.impl.JSR352JobServiceImpl;
import com.javabatchmanager.utils.JSR352DtoCreatorUtils;
import com.javabatchmanager.watchers.JSR352JobListener;

import org.junit.Assert;
import org.junit.Before;

@RunWith(Arquillian.class)
public class JSRJobServiceTest {
	
	
    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "java-batch-manager.war")
        		.addClass(JobService.class)
        		.addClass(JSR352JobServiceImpl.class)
        		.addPackage(BaseBatchException.class.getPackage())
        		.addClass(JobOperatorImpl.class)
        		.addPackage(PropertiesPersister.class.getPackage())
        		.addPackage(PropertiesConverter.class.getPackage())
        		.addPackage("com.javabatchmanager.dtos")
        		.addPackage(SimpleItemReader.class.getPackage())
        		.addClass(JSR352DtoCreatorUtils.class)
        		.addClass(JSR352JobListener.class)
        		.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
        		.addAsResource("META-INF/batch.xml")
        		.addAsResource("META-INF/batch-jobs/jsrtestjob.xml");
    }
	
    protected static JSR352JobServiceImpl jobService;

    

    
	@Test
	public void creatingJobServiceTest() {
		jobService = new JSR352JobServiceImpl();
		jobService.setJobOp();
		if(jobService==null){
			fail("JobService is null");
		}
	}
	
	//launching
	@Test
	public void launchingJobTest() throws BaseBatchException {
		Set<String>names=jobService.getJobNames();
		if(names.isEmpty()){
			fail("No jobs available for launching");
		}
		if(!names.contains("jsrtestjob")){
			fail("There should be job jsrtestjob available for launching");
		}
		JobExecutionDto jobExecDto=jobService.start("jsrtestjob", null);
		if(jobExecDto == null){
			fail("JobExecutionDto should not be null");
		}
		Long execid = jobExecDto.getJobExecutionId();
		JobExecutionDto jobExecDto2 = jobService.getJobExecutionById(execid);
		Assert.assertEquals(jobExecDto2.getJobName(), jobExecDto.getJobName());
		Assert.assertEquals(jobExecDto2.getJobExecutionId(), jobExecDto.getJobExecutionId());		
	}
	
	@Test
	public void launchingNonExistentJobTest(){
		try {
			JobExecutionDto jobExecDto = jobService.start("IDontExistJob", null);
			fail();
		} catch (BaseBatchException e) {
			String cause = e.causeEnum.name();
			if(!cause.equals("JOB_START")){
				Assert.fail("Wrong exception thrown");
			}
		}
		
	}
	
	//stopping
	@Test
	public void stoppingJobTest() throws BaseBatchException{
		JobExecutionDto jobExecDto = jobService.start("jsrtestjob", "key:value0");		
		jobExecDto = jobService.stop(jobExecDto.getJobExecutionId());
		jobExecDto = jobService.getJobExecutionById(jobExecDto.getJobExecutionId());
		String status = jobExecDto.getStatus();
		if("STOPPING".equals(status)==false && "STOPPED".equals(status)==false){
			Assert.fail("Status should be stopping or stopped");
		}
	}

	@Test
	public void stoppingNonExistentJobTest(){
		try {
			JobExecutionDto jobExecDto = jobService.stop(-1L);
			fail();
		} catch (BaseBatchException e) {
			String cause = e.causeEnum.name();
			if(!cause.equals("NO_SUCH_JOB_EXECUTION")){
				Assert.fail("Wrong exception thrown");
			}
		}
	}
	
	@Test
	public void stoppingNotRunningJobTest(){
		try{
			JobExecutionDto jobExec = jobService.start("jsrtestjob", "key:value1");
			jobExec = jobService.stop(jobExec.getJobExecutionId());
			jobExec = jobService.stop(jobExec.getJobExecutionId());
			if(!"STOPPING".equals(jobExec.getStatus())){
				fail();			
			}
		}catch (BaseBatchException e) {
			String cause = e.causeEnum.name();
			if(!cause.equals("JOB_EXECUTION_NOT_RUNNING")){
				Assert.fail("Wrong exception thrown");
			}
		}
	}
	
	
	//restarting
	@Test
	public void restartJobRightAfterStartTest(){
		try{
			JobExecutionDto jobExec = jobService.start("jsrtestjob", "key:value2");
			jobExec = jobService.stop(jobExec.getJobExecutionId());
			jobExec = jobService.restart(jobExec.getJobExecutionId());
		}catch(BaseBatchException ex){

		}
	}
	
	@Test
	public void restartJobAfterItReallyStopTest(){
		try{
			JobExecutionDto jobExec = jobService.start("jsrtestjob", "key:value3");
			jobExec = jobService.stop(jobExec.getJobExecutionId());
			while(!"STOPPED".equals(jobExec.getStatus())){
				Thread.sleep(500);
				jobExec = jobService.getJobExecutionById(jobExec.getJobExecutionId());
				if("COMPLETED".equals(jobExec.getStatus())){
					break;
				}
			}
			jobExec = jobService.restart(jobExec.getJobExecutionId());
		} catch(BaseBatchException ex){

		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}
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
	
	@Test
	public void restarCompletedJobTest(){
		try{
			JobExecutionDto jobExec = jobService.start("jsrtestjob", "key:value4");
			while(!"COMPLETED".equals(jobExec.getStatus())){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.err.println(e.getMessage());
				}
				jobExec = jobService.getJobExecutionById(jobExec.getJobExecutionId());
			}
			jobExec = jobService.restart(jobExec.getJobExecutionId());
		} catch(BaseBatchException ex){
			fail();
		}
	}
	//Get jobInstances
	@Test
	public void getJobInstancesOfNonExistentJobTest(){
		try{
			List<JobInstanceDto> instDtos = jobService.getJobInstances("IDontExistJob", 1, 0);
			if(!instDtos.isEmpty()){
				fail();
			}
		} catch(BaseBatchException ex){
			
		}
	}
	
	@Test
	public void getJobInstancesTest(){
		try{
			JobExecutionDto jobExec = jobService.start("jsrtestjob", "key:value6");
			List<JobInstanceDto> instDtos = jobService.getJobInstances("jsrtestjob", 0, 10);
			if(instDtos.isEmpty()){
				fail();
			}
		}catch(BaseBatchException ex){
			
		}
	}
	
	
	
	@Test
	public void getJobInstancesOfwithNegativeBoundariesTest(){
		try{
			List<JobInstanceDto> instDtos = jobService.getJobInstances("jsrtestjob", -10, -5);
			if(!instDtos.isEmpty()){
				fail();
			}
		} catch(BaseBatchException ex){
			fail();
		}
	}
	

	//executions
	@Test
	public void getJobExecutionsByInstanceTest(){
		try{
			JobExecutionDto jobExec = jobService.start("jsrtestjob", "key:value6");
			List<JobInstanceDto> instDtos = jobService.getJobInstances("jsrtestjob", 0, 10);
			List<JobExecutionDto> execsOfInstance = jobService.getJobExecutions(instDtos.get(0));
			if(execsOfInstance.isEmpty()){
				fail();
			}
		}catch(BaseBatchException ex){
			fail();
		}
	}
	
	@Test
	public void getJobExecutionByNullTest(){
		try{
			List<JobExecutionDto> execsOfInstance = jobService.getJobExecutions(null);
			Assert.assertEquals(true, execsOfInstance.isEmpty());
		}catch(BaseBatchException ex){
			fail();
		}		
	}
	
	@Test
	public void getRunningJobExecsNullTest(){
		try{
			Set<JobExecutionDto>execDtos = jobService.getRunningJobExecutions(null);
			Assert.assertEquals(true, execDtos.isEmpty());
		}catch (BaseBatchException ex){
			fail();
		}
	}
	
	
	
	@Before
	public void setUp(){
		if(jobService==null){
			jobService = new JSR352JobServiceImpl();
			jobService.setJobOp();
		}
	}
}
