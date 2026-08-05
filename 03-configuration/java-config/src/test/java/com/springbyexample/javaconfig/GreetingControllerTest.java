package com.springbyexample.javaconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class GreetingControllerTest {

    @Test
    void shouldLoadBeansFromJavaConfiguration() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingController controller = context.getBean(GreetingController.class);

            assertNotNull(controller);
            assertEquals("Hello from Java configuration!", controller.greet());
        }
    }
}
