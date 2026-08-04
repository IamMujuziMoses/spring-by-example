package com.springbyexample.importconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
public class GreetingConfig {

    @Bean
    GreetingService greetingService() {
        return new GreetingService();
    }
}
