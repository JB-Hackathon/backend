package com.example.jbbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JbBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(JbBackendApplication.class, args);
    }

}
