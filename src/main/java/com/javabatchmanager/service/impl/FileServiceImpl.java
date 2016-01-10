package com.javabatchmanager.service.impl;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jberet.job.model.XmlElement;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionStoreException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.javabatchmanager.dtos.FileUploadDto;
import com.javabatchmanager.service.FileService;

public class FileServiceImpl implements FileService, ApplicationContextAware {
	private ApplicationContext ctx;
	private final String SPRING_SAVED_FILE_PATH = System.getProperty("user.home") + File.separator + "javaBatchManUpJobs"
			+ File.separator + "uploadedJobContexts" + File.separator + "springJobs";

	private final String JSR_SAVED_FILE_PATH = System.getProperty("user.home") + File.separator + "javaBatchManUpJobs"
			+ File.separator + "uploadedJobContexts" + File.separator + "jsrJobs";

	@PostConstruct
	private void createDir() {
		File springfile = new File(SPRING_SAVED_FILE_PATH);
		File jsrfile = new File(JSR_SAVED_FILE_PATH);
		if (!springfile.exists()) {
			springfile.mkdirs();
		}
		if (!jsrfile.exists()) {
			jsrfile.mkdirs();
		}
		registerExistingContexts();

	}

	@Override
	public void saveFile(FileUploadDto fileDto) throws DuplicateJobException, XmlBeanDefinitionStoreException {

		ApplicationContext newJobAppContext = null;
		try {
			InputStream fileContentStream = fileDto.getFilePart().getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(fileDto.getFilePart().getInputStream()));
			String path = checkTypeOfJob(fileContentStream);

			if ("".equals(path)) {
				return;
			}
			File output = new File(new File(path), fileDto.getFilePart().getOriginalFilename());
			output.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(output));
			String line;
			while ((line = br.readLine()) != null) {
				bw.write(line);
				bw.flush();
			}
			bw.close();
			if (JSR_SAVED_FILE_PATH.equals(path)) {
				return;
			}
			try {
				newJobAppContext = new ClassPathXmlApplicationContext(new String[] { "file:" + output }, ctx);
				Map<String, Job> jobs = newJobAppContext.getBeansOfType(Job.class);
				for (Map.Entry<String, Job> entry : jobs.entrySet()) {
					final JobFactory jobFactory = new ReferenceJobFactory(entry.getValue());
					JobRegistry jobRegister = (JobRegistry) ctx.getBean("jobRegistry");
					jobRegister.register(jobFactory);
				}
			} catch (BeansException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			System.out.println(e.getMessage() + " " + e.getCause());
		} catch (XMLStreamException e) {
			System.out.println(e.getMessage() + " " + e.getCause());
		} finally {
			if (newJobAppContext != null) {
				((ClassPathXmlApplicationContext) newJobAppContext).close();
			}
		}
	}

	public void registerExistingContexts() {
		File fileMetaInf = new File(SPRING_SAVED_FILE_PATH);
		File[] jobDefinitions = fileMetaInf.listFiles();
		for (File job : jobDefinitions) {
			ApplicationContext newJobAppContext = null;
			try {
				newJobAppContext = new ClassPathXmlApplicationContext(new String[] { "file:" + job }, ctx);
				Map<String, Job> jobs = newJobAppContext.getBeansOfType(Job.class);
				for (Map.Entry<String, Job> entry : jobs.entrySet()) {
					final JobFactory jobFactory = new ReferenceJobFactory(entry.getValue());
					JobRegistry jobRegister = (JobRegistry) ctx.getBean("jobRegistry");
					jobRegister.register(jobFactory);
				}
			} catch (BeansException e) {
				e.printStackTrace();
			} catch (DuplicateJobException e) {
				e.printStackTrace();
			} finally {
				if (newJobAppContext != null) {
					((ClassPathXmlApplicationContext) newJobAppContext).close();
				}
			}
		}
	}

	public String checkTypeOfJob(InputStream is) throws XMLStreamException, FactoryConfigurationError {
		final XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(is);
		try {
			while (reader.hasNext()) {
				final int eventType = reader.next();
				if (eventType != START_ELEMENT && eventType != END_ELEMENT) {
					continue;
				}
				final XmlElement element = XmlElement.forName(reader.getLocalName());
				if (eventType == START_ELEMENT) {
					if (element == XmlElement.JOB) {
						String type = reader.getNamespaceURI(0);
						if (type.contains("springframework.org/schema/batch")) {
							return SPRING_SAVED_FILE_PATH;
						}
						if (type.contains("jcp.org/xml/ns/javaee")) {
							return JSR_SAVED_FILE_PATH;
						}
					}
				}

			}
		} catch (XMLStreamException ex) {
			ex.printStackTrace();
		}
		return "";
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}
}
