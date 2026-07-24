package com.springbyexample.fieldinjection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
public class AppConfig {

    @Bean
    public EmailService emailService() {
        return new EmailService();
    }

    @Bean
    public NotificationService notificationService() {
        return new NotificationService();
    }

}