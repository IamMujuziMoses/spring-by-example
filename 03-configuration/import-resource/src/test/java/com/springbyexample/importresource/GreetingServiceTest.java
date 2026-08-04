package com.springbyexample.importresource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class GreetingServiceTest {

    @Test
    void shouldImportXmlConfiguration() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService service = context.getBean(GreetingService.class);

            assertNotNull(service);
            assertEquals("Hello from XML configuration!", service.greet());
        }
    }
}
