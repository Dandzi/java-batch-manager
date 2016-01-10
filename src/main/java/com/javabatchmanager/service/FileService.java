package com.javabatchmanager.service;

import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionStoreException;

import com.javabatchmanager.dtos.FileUploadDto;

public interface FileService {
	
	public void saveFile(FileUploadDto file) throws DuplicateJobException,XmlBeanDefinitionStoreException;
}
