package com.challenge;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CodingChallengeApplication {

	public static void main(String[] args) {
//		BasicConfigurator.configure();
		SpringApplication.run(CodingChallengeApplication.class, args);
	}

}
