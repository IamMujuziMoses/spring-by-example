package com.springbyexample.factorybean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
public class FactoryBeanConfig {

    @Bean
    public GreetingServiceFactoryBean greetingService() {
        return new GreetingServiceFactoryBean();
    }
}
