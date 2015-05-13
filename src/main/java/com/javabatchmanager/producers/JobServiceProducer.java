package com.javabatchmanager.producers;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.AutomaticJobRegistrar;
import org.springframework.batch.core.configuration.support.ClasspathXmlApplicationContextsFactoryBean;
import org.springframework.batch.core.configuration.support.DefaultJobLoader;
import org.springframework.batch.core.configuration.support.JobLoader;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.javabatchmanager.service.JobService;
import com.javabatchmanager.service.impl.JobServiceImpl;


public class JobServiceProducer {
	
	private JobLoader jobLoader;
	
	private JobLauncher jobLauncher;
	
	private JobRepository jobRepository;
	
	private JobRegistry jobRegistry;

	private AutomaticJobRegistrar automaticJobRegistrar;
	
	public JobService getJobService() throws Exception{
		return createJobServiceImplWithParams(createPlainJobServiceImpl());
	}
	
	public JobServiceImpl createPlainJobServiceImpl(){
		return new JobServiceImpl();
	}
	
	public JobServiceImpl createJobServiceImplWithParams(JobServiceImpl jobServiceImpl) throws Exception{
		jobServiceImpl.setJobLauncher(jobLauncher);
		jobServiceImpl.setJobLoader(jobLoader);
		jobServiceImpl.setJobRegistry(jobRegistry);
		jobServiceImpl.setJobRepository(jobRepository);

		jobServiceImpl.setAutomaticJobRegistrar(automaticJobRegistrar);
			
		ClasspathXmlApplicationContextsFactoryBean contextFactory = new ClasspathXmlApplicationContextsFactoryBean();
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new org.springframework.core.io.support.PathMatchingResourcePatternResolver();
		Resource[] resources = pathMatchingResourcePatternResolver.getResources("classpath*:/META-INF/batchjobs/*.xml");
		contextFactory.setResources(resources);
		
		this.automaticJobRegistrar.setApplicationContextFactories(contextFactory.getObject());
		this.automaticJobRegistrar.setJobLoader(this.jobLoader);
		if(this.jobLoader instanceof DefaultJobLoader){
			((DefaultJobLoader) jobLoader).setJobRegistry(jobRegistry);
		}
		if(this.jobLauncher instanceof SimpleJobLauncher){
			((SimpleJobLauncher) jobLauncher).setJobRepository(jobRepository);
		}
			
		return jobServiceImpl;		
	}
}
