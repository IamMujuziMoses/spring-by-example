package com.springbyexample.configurationclasspostprocessor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
public class AdditionalConfig {

    @Bean
    public String applicationName() {
        return "Spring by Example";
    }
}
