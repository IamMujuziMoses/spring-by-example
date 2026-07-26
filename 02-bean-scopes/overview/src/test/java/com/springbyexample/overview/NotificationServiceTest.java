package com.springbyexample.overview;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class NotificationServiceTest {

    @Test
    void shouldCreateApplicationContext() {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        assertNotNull(context);
    }

    @Test
    void shouldCreateNotificationServiceBean() {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        NotificationService service = context.getBean(NotificationService.class);
        assertNotNull(service);
        service.notify("Welcome to Bean Scopes!");
    }
}
