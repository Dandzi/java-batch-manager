package com.javabatchmanager.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.Part;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionStoreException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.javabatchmanager.dtos.FileUploadDto;
import com.javabatchmanager.service.FileService;

@Service
public class FileServiceImpl implements FileService, ApplicationContextAware {
	private ApplicationContext ctx;
	private ClassLoader classLoader = getClass().getClassLoader();
	private final String SAVED_FILE_PATH = System.getProperty("user.home")
			+ File.separator + "javaBatchManager" + File.separator
			+ "uploadedJobContexts";

	@PostConstruct
	private void createDir() {
		if (!fileExists("")) {
			File file = new File(SAVED_FILE_PATH);
			file.mkdirs();
		}
	}

	public boolean fileExists(String name) {
		try {
			File f = new File(classLoader.getResource(SAVED_FILE_PATH + name).getFile());
			if (f.exists() && f.isFile()) {
				return true;
			}
		} catch (NullPointerException ex) {
			return false;
		}
		return false;
	}

	@Override
	public void saveFile(FileUploadDto fileDto) throws DuplicateJobException,
			XmlBeanDefinitionStoreException {
		//MultipartFile filePart = fileDto.getFilePart();
		ApplicationContext newJobAppContext = null;
		try {
			//File parent = new File(SAVED_FILE_PATH);
			File output = new File(new File(SAVED_FILE_PATH), fileDto.getFilePart().getOriginalFilename());

			output.createNewFile();
			InputStream fileContent = fileDto.getFilePart().getInputStream();
			ByteStreams.copy(fileContent, Files.newOutputStreamSupplier(output));
			
			newJobAppContext = new ClassPathXmlApplicationContext(new String[] { "file:" + output }, ctx);
			Map<String, Job> jobs = newJobAppContext.getBeansOfType(Job.class);
			for (Map.Entry<String, Job> entry : jobs.entrySet()) {
				final JobFactory jobFactory = new ReferenceJobFactory(entry.getValue());
				JobRegistry jobRegister = (JobRegistry) ctx.getBean("jobRegistry");
				jobRegister.register(jobFactory);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (newJobAppContext != null) {
				((ClassPathXmlApplicationContext) newJobAppContext).close();
			}
		}
	}



	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;
	}
}
