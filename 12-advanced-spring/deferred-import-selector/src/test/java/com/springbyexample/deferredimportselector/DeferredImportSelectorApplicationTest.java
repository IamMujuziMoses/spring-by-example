package com.springbyexample.deferredimportselector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class DeferredImportSelectorApplicationTest {

    @Test
    void shouldImportSelectedConfiguration() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(ApplicationConfig.class)) {

            GreetingService greetingService = applicationContext.getBean(GreetingService.class);

            assertNotNull(greetingService);
            assertEquals("Hello from DeferredImportSelector!", greetingService.greet());
        }
    }
}