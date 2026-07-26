package com.springbyexample.constructorinjection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */

public class NotificationServiceTest {

    @Test
    void shouldInjectEmailServiceUsingConstructor() {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        NotificationService service = context.getBean(NotificationService.class);
        assertNotNull(service);
        service.notify("Hello Spring!");
    }
}
