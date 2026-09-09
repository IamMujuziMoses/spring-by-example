package com.springbyexample.importselector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ImportSelectorApplicationTest {

    @Test
    void shouldImportSelectedConfiguration() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(ApplicationConfig.class)) {

            GreetingService greetingService = applicationContext.getBean(GreetingService.class);

            assertNotNull(greetingService);
            assertEquals("Hello from ImportSelector!", greetingService.greet());
        }
    }
}
