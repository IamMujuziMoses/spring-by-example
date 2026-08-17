package com.springbyexample.whatisaop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
@ComponentScan
public class AppConfig {

    @Bean
    public LoggingService loggingService() {
        return new LoggingService();
    }

    @Bean
    public OrderService orderService(LoggingService loggingService) {
        return new OrderService(loggingService);
    }
}
