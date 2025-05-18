package com.fitness.aiservice;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiserviceApplication {
//	@PostConstruct
//	public void printEnv() {
//		System.out.println("GEMINI_API_URL = " + System.getenv("GEMINI_API_URL"));
//		System.out.println("GEMINI_API_KEY = " + System.getenv("GEMINI_API_KEY"));
//	}

	public static void main(String[] args) {
		SpringApplication.run(AiserviceApplication.class, args);


	}

}
