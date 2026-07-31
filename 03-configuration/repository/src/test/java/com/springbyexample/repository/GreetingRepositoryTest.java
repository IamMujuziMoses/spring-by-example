package com.springbyexample.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class GreetingRepositoryTest {

    @Test
    void shouldDiscoverRepositoryAutomatically() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingRepository repository = context.getBean(GreetingRepository.class);

            assertNotNull(repository);
            assertEquals("Hello from GreetingRepository!", repository.findGreeting());
        }
    }
}
