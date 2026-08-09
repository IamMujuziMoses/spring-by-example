package com.springbyexample.beanaliases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class NotificationServiceTest {

    @Test
    void shouldResolveSameBeanUsingDifferentAliases() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            NotificationService service = context.getBean("notificationService", NotificationService.class);
            NotificationService emailService = context.getBean("emailNotificationService", NotificationService.class);

            assertSame(service, emailService);
        }
    }

    @Test
    void shouldRegisterBothBeanNames() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            assertTrue(context.containsBean("notificationService"));
            assertTrue(context.containsBean("emailNotificationService"));
        }
    }
}
