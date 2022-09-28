package com.project.integration.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.project.integration")
public class BigProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BigProjectApplication.class, args);
	}
}
