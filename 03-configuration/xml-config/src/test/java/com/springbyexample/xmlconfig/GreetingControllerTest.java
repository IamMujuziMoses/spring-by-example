package com.springbyexample.xmlconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class GreetingControllerTest {

    @Test
    void shouldLoadBeansFromXmlConfiguration() {

        try (var context = new ClassPathXmlApplicationContext("applicationContext.xml")) {

            GreetingController controller = context.getBean(GreetingController.class);

            assertNotNull(controller);
            assertEquals("Hello from XML configuration!", controller.greet());
        }
    }
}
