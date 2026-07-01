package com.javabuilder.backendservice.configuration;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfiguration {

    @Value("${sendgrid.api-key}")
    private String apiKey;

    @Bean
    SendGrid sendGrid() {
        return new SendGrid(apiKey);
    }
}
