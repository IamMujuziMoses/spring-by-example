package com.springbyexample.hellobean;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 *
 */

public class AppConfigTest {

    @Test
    void shouldCreateHelloServiceBean() {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        HelloService service = context.getBean(HelloService.class);
        assertNotNull(service);

    }
}
