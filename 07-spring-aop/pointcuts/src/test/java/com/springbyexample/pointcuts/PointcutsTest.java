package com.springbyexample.pointcuts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class PointcutsTest {

    @Test
    void shouldCreateAopProxy() {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            assertTrue(AopUtils.isAopProxy(greetingService));
        }
    }

    @Test
    void shouldReturnGreeting() {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            assertEquals("Hello, Spring!", greetingService.greet("Spring"));
        }
    }

    @Test
    void shouldReturnFarewell() {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            assertEquals("Goodbye, Spring!", greetingService.farewell("Spring"));
        }
    }

}
