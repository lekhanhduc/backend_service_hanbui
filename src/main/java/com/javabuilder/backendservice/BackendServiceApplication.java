package com.javabuilder.backendservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BackendServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(BackendServiceApplication.class, args);
    }

}
