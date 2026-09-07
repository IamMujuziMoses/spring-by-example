package com.springbyexample.applicationcontext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class ApplicationContextApplicationTest {

    @Test
    void shouldCreateAndRetrieveBean() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(ApplicationContextConfig.class)) {

            GreetingService greetingService = applicationContext.getBean(GreetingService.class);

            assertNotNull(greetingService);
            assertEquals("Hello from ApplicationContext!", greetingService.greet());
        }
    }

    @Test
    void shouldReturnSameSingletonInstance() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(ApplicationContextConfig.class)) {

            GreetingService first = applicationContext.getBean(GreetingService.class);
            GreetingService second = applicationContext.getBean(GreetingService.class);

            assertSame(first, second);
        }
    }
}
