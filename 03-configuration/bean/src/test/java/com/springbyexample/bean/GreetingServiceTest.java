package com.springbyexample.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class GreetingServiceTest {

    @Test
    void shouldRegisterBeansUsingBeanAnnotation() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            GreetingService greetingService = context.getBean(GreetingService.class);
            TimeService timeService = context.getBean(TimeService.class);
            assertNotNull(greetingService);
            assertNotNull(timeService);
            assertEquals("Hello from GreetingService!", greetingService.greet());
        }
    }

    @Test
    void shouldUseMethodNamesAsBeanNames() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            assertTrue(context.containsBean("greetingService"));
            assertTrue(context.containsBean("timeService"));
        }
    }
}
