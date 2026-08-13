package com.springbyexample.publishingevents;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
public class AppConfig {

    @Bean
    public OrderService orderService(ApplicationEventPublisher eventPublisher) {
        return new OrderService(eventPublisher);
    }
}
