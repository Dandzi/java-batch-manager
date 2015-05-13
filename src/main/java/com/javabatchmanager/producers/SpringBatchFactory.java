package com.javabatchmanager.producers;

import java.io.IOException;
import java.io.Serializable;



import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.AutomaticJobRegistrar;
import org.springframework.batch.core.configuration.support.ClasspathXmlApplicationContextsFactoryBean;
import org.springframework.batch.core.configuration.support.DefaultJobLoader;
import org.springframework.batch.core.configuration.support.JobLoader;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;




//@Named
public class SpringBatchFactory implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6039614306870641651L;

	
	//@Produces
	public JobLauncher getSimpleJobLauncher() throws Exception{
		SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
		return simpleJobLauncher;
	}

	//@Produces
	public JobRepository getMapJobRepository() throws Exception{
		MapJobRepositoryFactoryBean factory =  new MapJobRepositoryFactoryBean();
		return (JobRepository) factory.getObject();
	}
	
	//@Produces
	public JobRegistry getJobRegistry(){
		return new MapJobRegistry();
	}
	
	//@Produces
	public AutomaticJobRegistrar getAutomaticJobRegistrar() throws Exception{
		AutomaticJobRegistrar automaticJobRegistrar = new AutomaticJobRegistrar();		
		return automaticJobRegistrar;		
	}
	
	//@Produces
	public ClasspathXmlApplicationContextsFactoryBean getAppContextFactoryBean() throws IOException{
		ClasspathXmlApplicationContextsFactoryBean contextFactory = new ClasspathXmlApplicationContextsFactoryBean();
		//PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new org.springframework.core.io.support.PathMatchingResourcePatternResolver();
		//Resource[] resources = pathMatchingResourcePatternResolver.getResources("classpath*:/META-INF/batchjobs/*.xml");
		//contextFactory.setResources(resources);
		return contextFactory;		
	}
	
	//@Produces
	public JobLoader getJobLoader(){
		DefaultJobLoader defJobLoader = new DefaultJobLoader();
		//JobRegistry jobRegistry = getJobRegistry();
		//defJobLoader.setJobRegistry(jobRegistry);
		return defJobLoader;
	}
	
	
	
}
