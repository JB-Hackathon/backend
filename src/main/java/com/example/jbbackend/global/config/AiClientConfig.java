package com.example.jbbackend.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AiClientConfig {

    @Bean
    public RestClient aiRestClient(
        @Value("${ai.service.base-url}") String aiServiceBaseUrl
    ) {
        return RestClient.builder()
            .baseUrl(aiServiceBaseUrl)
            .build();
    }
}
