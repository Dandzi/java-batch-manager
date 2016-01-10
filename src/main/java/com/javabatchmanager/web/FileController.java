package com.javabatchmanager.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionStoreException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.javabatchmanager.dtos.FileUploadDto;
import com.javabatchmanager.dtos.JobInstanceDto;
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
		if (!fileService.fileExists(file.getFilePart().getOriginalFilename())) {
			try {
				fileService.saveFile(file);
			} catch (DuplicateJobException ex) {
				errors.rejectValue("jobName", "job.file.error.exist");
			} catch (XmlBeanDefinitionStoreException ex) {
				errors.rejectValue("jobName", "job.file.error.not.context");
			}
		}
		return "job-upload";
	}
}
