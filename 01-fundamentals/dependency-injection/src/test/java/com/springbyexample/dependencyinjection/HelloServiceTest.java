package com.springbyexample.dependencyinjection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */

public class HelloServiceTest {

    @Test
    void shouldCreateHelloServiceBean() {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        HelloService helloService = context.getBean(HelloService.class);
        assertNotNull(helloService);
    }

    @Test
    void shouldInjectGreetingServiceIntoHelloService() {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        HelloService helloService = context.getBean(HelloService.class);
        assertNotNull(helloService);

        // If Spring failed to inject GreetingService,
        // calling sayHello() would fail.
        helloService.sayHello();
    }
}
