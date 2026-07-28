package com.springbyexample.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
public class AppConfig {

    @Bean
    public GreetingService greetingService() {
        return new GreetingService();
    }

    @Bean
    public TimeService timeService() {
        return new TimeService();
    }

}
