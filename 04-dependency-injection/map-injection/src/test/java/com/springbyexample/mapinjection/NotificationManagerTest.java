package com.springbyexample.mapinjection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class NotificationManagerTest {

    @Test
    void shouldInjectBeansIntoMap() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            NotificationManager manager = context.getBean(NotificationManager.class);

            Map<String, NotificationService> services = manager.getNotificationServices();

            assertEquals(3, services.size());

            assertTrue(services.containsKey("emailNotificationService"));
            assertTrue(services.containsKey("smsNotificationService"));
            assertTrue(services.containsKey("pushNotificationService"));
        }
    }
}
