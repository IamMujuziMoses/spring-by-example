package com.springbyexample.hellobean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */

@Configuration
public class AppConfig {

    @Bean
    public HelloService helloService() {
        return new HelloService();
    }
}
