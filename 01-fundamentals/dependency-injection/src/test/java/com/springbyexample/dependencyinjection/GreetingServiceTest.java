package com.springbyexample.dependencyinjection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */

public class GreetingServiceTest {

    @Test
    void shouldCreateGreetingServiceBean() {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        GreetingService greetingService = context.getBean(GreetingService.class);
        assertNotNull(greetingService);

    }
}
