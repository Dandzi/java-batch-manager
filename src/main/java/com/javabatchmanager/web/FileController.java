package com.javabatchmanager.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionStoreException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javabatchmanager.dtos.FileUploadDto;
import com.javabatchmanager.service.FileService;

@Controller
@RequestMapping("/file-upload")
public class FileController {

	@Autowired
	private FileService fileService;

	@ModelAttribute("file")
	public FileUploadDto getFile(HttpServletRequest request, ModelMap model) {
		FileUploadDto file = new FileUploadDto();
		model.addAttribute("file", file);
		return file;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getFileUpload() {
		return "job-upload";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String saveFileUpload(@ModelAttribute("file") FileUploadDto file,
			BindingResult errors) throws IOException, ServletException,
			DuplicateJobException {
			try {
				fileService.saveFile(file);
			} catch (DuplicateJobException ex) {
				ex.printStackTrace();
				errors.rejectValue("name", "job.file.error.exist");
			} catch (XmlBeanDefinitionStoreException ex) {
				ex.printStackTrace();
				errors.rejectValue("name", "job.file.error.not.context");
			}
		return "job-upload";
	}
}
