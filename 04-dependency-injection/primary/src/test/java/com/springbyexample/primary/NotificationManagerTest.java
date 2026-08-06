package com.springbyexample.primary;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class NotificationManagerTest {

    @Test
    void shouldInjectPrimaryBean() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            // Retrieve the component that depends on NotificationService.
            NotificationManager manager = context.getBean(NotificationManager.class);

            // Spring injects the bean marked with @Primary when multiple
            // NotificationService implementations are available.
            assertInstanceOf(EmailNotificationService.class, manager.getNotificationService());
        }
    }
}
