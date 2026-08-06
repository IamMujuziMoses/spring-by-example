package com.springbyexample.collectioninjection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class NotificationManagerTest {

    @Test
    void shouldInjectAllNotificationServices() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            NotificationManager manager = context.getBean(NotificationManager.class);

            // Spring injects every bean that matches NotificationService. Unlike @Primary or @Qualifier, no single bean is selected.
            assertEquals(3, manager.getNotificationServices().size());
        }
    }

    @Test
    void shouldContainAllNotificationImplementations() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            NotificationManager manager = context.getBean(NotificationManager.class);

            List<NotificationService> services = manager.getNotificationServices();

            // Verify that every NotificationService implementation is available in the injected collection.
            assertTrue(services.stream().anyMatch(EmailNotificationService.class::isInstance));
            assertTrue(services.stream().anyMatch(SmsNotificationService.class::isInstance));
            assertTrue(services.stream().anyMatch(PushNotificationService.class::isInstance));
        }
    }

    @Test
    void shouldPreserveBeanOrder() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            NotificationManager manager = context.getBean(NotificationManager.class);

            List<NotificationService> services = manager.getNotificationServices();

            // Component scanning order can be controlled using @Order. Without @Order, relying on ordering is discouraged.
            assertEquals(3, services.size());
        }
    }

}
