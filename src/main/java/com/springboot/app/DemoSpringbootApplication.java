package com.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.springboot.app.models.service.IUploadFileService;

@SpringBootApplication
public class DemoSpringbootApplication implements CommandLineRunner {

	@Autowired
	private IUploadFileService uploadFileService;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringbootApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		uploadFileService.deleteAll();
		uploadFileService.init();

//		String pass = "12345";
//
//		for (int i = 0; i < 3; i++) {
//			String bcryptPassword = passwordEncoder.encode(pass);
//			System.out.println(bcryptPassword);
//		}

	}

}
