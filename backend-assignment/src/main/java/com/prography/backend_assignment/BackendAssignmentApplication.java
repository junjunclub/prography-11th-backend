package com.prography.backend_assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BackendAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendAssignmentApplication.class, args);
	}

}
