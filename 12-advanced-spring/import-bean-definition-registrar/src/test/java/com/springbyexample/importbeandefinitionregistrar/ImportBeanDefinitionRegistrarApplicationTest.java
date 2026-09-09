package com.springbyexample.importbeandefinitionregistrar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class ImportBeanDefinitionRegistrarApplicationTest {

    @Test
    void shouldRegisterGreetingService() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(ApplicationConfig.class)) {

            GreetingService greetingService = applicationContext.getBean(GreetingService.class);

            assertNotNull(greetingService);
            assertEquals("Hello from ImportBeanDefinitionRegistrar!", greetingService.greet());
        }
    }
}
