package com.springbyexample.componentscan;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class AppConfigTest {

    @Test
    void shouldDiscoverAllComponentsAutomatically() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            assertNotNull(context.getBean(GreetingController.class));
            assertNotNull(context.getBean(GreetingService.class));
            assertNotNull(context.getBean(GreetingRepository.class));
        }
    }
}
