package com.springbyexample.applicationcontext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
public class ApplicationContextConfig {

    @Bean
    public GreetingService greetingService() {
        return new GreetingService();
    }
}
