package com.springbyexample.fieldinjection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class NotificationServiceTest {

    @Test
    void shouldCreateNotificationServiceBean() {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        NotificationService service = context.getBean(NotificationService.class);
        assertNotNull(service);
        service.notify("Hello Spring!");

    }

    @Test
    void shouldInjectEmailServiceUsingFieldInjection() {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        NotificationService notificationService = context.getBean(NotificationService.class);
        assertNotNull(notificationService);

        // If Spring failed to inject EmailService,
        // calling notify() would fail.
        notificationService.notify("Welcome to Spring!");
    }
}
