package com.example.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Application entry point for the Personal Task Manager API.
 *
 * Scans Spring components and configuration properties used by the AI client.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class TaskManagerApplication {

	/**
	 * Bootstraps the Spring Boot runtime.
	 *
	 * @param args command-line arguments passed by the host process
	 */
	public static void main(String[] args) {
		SpringApplication.run(TaskManagerApplication.class, args);
	}

}

