package com.springbyexample.dependencyinjection;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mujuzi Moses
 */

class AppConfigTest {

    @Test
    void shouldCreateGreetingServiceBean() {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        GreetingService greetingService = context.getBean(GreetingService.class);
        assertNotNull(greetingService);

    }

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
