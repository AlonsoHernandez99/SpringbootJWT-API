package com.springboot.app.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {
	
	public Resource loadFile(String filename) throws MalformedURLException;
	
	public String copyFile(MultipartFile file) throws IOException;
	
	public boolean deleteFile(String filename);
	
	public void deleteAll();
	
	public void init() throws IOException;
}
