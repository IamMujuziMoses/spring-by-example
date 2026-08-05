package com.springbyexample.javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
public class AppConfig {

    @Bean
    GreetingRepository greetingRepository() {
        return new GreetingRepository();
    }

    @Bean
    GreetingService greetingService() {
        return new GreetingService(greetingRepository());
    }

    @Bean
    GreetingController greetingController() {
        return new GreetingController(greetingService());
    }

}
