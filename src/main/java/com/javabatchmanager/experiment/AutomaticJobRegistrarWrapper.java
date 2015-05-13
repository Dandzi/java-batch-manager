package com.javabatchmanager.experiment;

import java.io.Serializable;


import org.springframework.batch.core.configuration.support.AutomaticJobRegistrar;
import org.springframework.batch.core.configuration.support.ClasspathXmlApplicationContextsFactoryBean;
import org.springframework.batch.core.configuration.support.JobLoader;

//@ApplicationScoped
//@Named
public class AutomaticJobRegistrarWrapper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5376723393027829111L;

	private AutomaticJobRegistrar automaticJobRegistrar;

	//@Inject
	private transient JobLoader jobLoader;

	//@Inject
	private transient ClasspathXmlApplicationContextsFactoryBean contextFactory;

	public AutomaticJobRegistrar getAutomaticJobRegistrar() {
		return automaticJobRegistrar;
	}

	public void setAutomaticJobRegistrar(
			AutomaticJobRegistrar automaticJobRegistrar) {
		this.automaticJobRegistrar = automaticJobRegistrar;
	}
	
	public void createNewAutomaticJobRegistrar() throws Exception {
		if (this.automaticJobRegistrar == null) {
			this.automaticJobRegistrar = new AutomaticJobRegistrar();
			this.automaticJobRegistrar.setJobLoader(jobLoader);
			this.automaticJobRegistrar.setApplicationContextFactories(contextFactory
					.getObject());
		}
	}

}
