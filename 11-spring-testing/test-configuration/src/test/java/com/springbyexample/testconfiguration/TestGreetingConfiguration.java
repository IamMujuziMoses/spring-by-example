package com.springbyexample.testconfiguration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * @author Mujuzi Moses
 */
@TestConfiguration
public class TestGreetingConfiguration {

    @Bean
    @Primary
    GreetingProvider greetingProvider() {
        return () -> "Hello from the test!";
    }
}
