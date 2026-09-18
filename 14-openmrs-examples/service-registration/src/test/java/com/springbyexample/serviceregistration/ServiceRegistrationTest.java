package com.springbyexample.serviceregistration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.springbyexample.serviceregistration.impl.GreetingServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Mujuzi Moses
 */
@ComponentScan("com.springbyexample.serviceregistration")
public class ServiceRegistrationTest {

    @Test
    void shouldRegisterServiceAsSpringBean() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(ServiceRegistrationTest.class)) {

            GreetingService greetingService = context.getBean("greetingService", GreetingService.class);

            assertNotNull(greetingService);
            assertEquals(GreetingServiceImpl.class, greetingService.getClass());
        }
    }

    @Test
    void shouldReturnSameServiceInstance() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(ServiceRegistrationTest.class)) {

            GreetingService first = context.getBean("greetingService", GreetingService.class);
            GreetingService second = context.getBean("greetingService", GreetingService.class);

            assertSame(first, second);
        }
    }

    @Test
    void shouldInvokeRegisteredService() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(ServiceRegistrationTest.class)) {

            GreetingService greetingService = context.getBean("greetingService", GreetingService.class);

            assertEquals("Hello from an OpenMRS service!", greetingService.greet());
        }
    }
}
