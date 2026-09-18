package com.springbyexample.configurationclasspostprocessor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Mujuzi Moses
 */
@Configuration
@ComponentScan("com.springbyexample.configurationclasspostprocessor")
@Import(AdditionalConfig.class)
public class AppConfig {

    @Bean
    public GreetingController greetingController(GreetingService greetingService) {
        return new GreetingController(greetingService);
    }
}
