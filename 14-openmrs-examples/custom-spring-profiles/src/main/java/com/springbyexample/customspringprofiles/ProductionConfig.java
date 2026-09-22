package com.springbyexample.customspringprofiles;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Mujuzi Moses
 */
@Configuration
@Profile("production")
public class ProductionConfig {

    @Bean
    public GreetingService greetingService() {
        return new ProductionGreetingService();
    }
}
