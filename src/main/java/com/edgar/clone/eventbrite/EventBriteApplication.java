package com.edgar.clone.eventbrite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventBriteApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventBriteApplication.class, args);
	}

}
