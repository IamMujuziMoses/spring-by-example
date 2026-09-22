package com.springbyexample.customspringprofiles;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Mujuzi Moses
 */
@Configuration
@Profile("development")
public class DevelopmentConfig {

    @Bean
    public GreetingService greetingService() {
        return new DevelopmentGreetingService();
    }
}
