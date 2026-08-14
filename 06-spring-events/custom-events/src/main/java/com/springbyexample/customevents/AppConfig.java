package com.springbyexample.customevents;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
public class AppConfig {

    @Bean
    public PaymentService paymentService(ApplicationEventPublisher eventPublisher) {

        return new PaymentService(eventPublisher);
    }

    @Bean
    public PaymentNotificationService paymentNotificationService() {
        return new PaymentNotificationService();
    }
}
