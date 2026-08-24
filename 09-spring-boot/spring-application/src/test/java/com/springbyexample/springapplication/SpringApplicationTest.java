package com.springbyexample.springapplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class SpringApplicationTest {

    @Test
    void shouldStartApplicationContext() {
        try (ConfigurableApplicationContext context = SpringApplication.run(Application.class)) {

            assertNotNull(context);
        }
    }

    @Test
    void shouldRegisterGreetingService() {
        try (ConfigurableApplicationContext context = SpringApplication.run(Application.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            assertNotNull(greetingService);
        }
    }

    @Test
    void shouldReturnGreeting() {
        try (ConfigurableApplicationContext context = SpringApplication.run(Application.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            assertEquals("Hello, Spring!", greetingService.greet("Spring"));
        }
    }
}
