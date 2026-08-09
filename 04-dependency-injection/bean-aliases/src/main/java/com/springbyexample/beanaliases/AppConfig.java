package com.springbyexample.beanaliases;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
public class AppConfig {

    @Bean(name = {"notificationService", "emailNotificationService"})
    public NotificationService notificationService() {
        return new NotificationService();
    }
}
