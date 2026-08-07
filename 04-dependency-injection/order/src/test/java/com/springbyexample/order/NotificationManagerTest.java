package com.springbyexample.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class NotificationManagerTest {

    @Test
    void notificationServices_shouldBeInjectedInOrder() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            NotificationManager manager = context.getBean(NotificationManager.class);

            List<String> services = manager.getNotificationServices().stream().map(NotificationService::getName).toList();

            assertEquals(List.of("SMS", "Push", "Email"), services);
        }
    }

}
