package com.springbyexample.xmltojavaconfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.springbyexample.xmltojavaconfiguration.impl.GreetingServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class XmlToJavaConfigurationTest {

    @Test
    void shouldRegisterBeanFromXmlConfiguration() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class)) {

            GreetingService greetingService = context.getBean("greetingService", GreetingService.class);

            assertNotNull(greetingService);
            assertEquals(GreetingServiceImpl.class, greetingService.getClass());
        }
    }

    @Test
    void shouldRegisterBeanFromJavaConfiguration() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class)) {

            GreetingService greetingService = context.getBean("javaGreetingService", GreetingService.class);

            assertNotNull(greetingService);
            assertEquals(GreetingServiceImpl.class, greetingService.getClass());
        }
    }

    @Test
    void shouldInvokeXmlConfiguredService() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class)) {

            GreetingService greetingService = context.getBean("greetingService", GreetingService.class);

            assertEquals("Hello from a configured service!", greetingService.greet()
            );
        }
    }

    @Test
    void shouldInvokeJavaConfiguredService() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class)) {

            GreetingService greetingService = context.getBean("javaGreetingService", GreetingService.class);

            assertEquals("Hello from a configured service!", greetingService.greet());
        }
    }

    @Test
    void shouldReturnSameXmlConfiguredInstance() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class)) {

            GreetingService first = context.getBean("greetingService", GreetingService.class);
            GreetingService second = context.getBean("greetingService", GreetingService.class);

            assertSame(first, second);
        }
    }

    @Test
    void shouldReturnSameJavaConfiguredInstance() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class)) {

            GreetingService first = context.getBean("javaGreetingService", GreetingService.class);
            GreetingService second = context.getBean("javaGreetingService", GreetingService.class);

            assertSame(first, second);
        }
    }
}
