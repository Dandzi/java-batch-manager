package com.javabatchmanager.dtos;

import org.springframework.web.multipart.MultipartFile;

/*
 * Object used for uploading the file.
 */
public class FileUploadDto {
	private String name;
	private MultipartFile filePart;

	public MultipartFile getFilePart() {
		return filePart;
	}

	public void setFilePart(MultipartFile filePart) {
		this.filePart = filePart;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
