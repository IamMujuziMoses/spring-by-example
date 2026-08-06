package com.springbyexample.qualifier;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class NotificationManagerTest {

    @Test
    void shouldInjectQualifiedBean() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            // Retrieve the component that depends on NotificationService.
            NotificationManager manager = context.getBean(NotificationManager.class);

            // Although multiple NotificationService implementations exist, @Qualifier instructs Spring to
            // inject the specifically named bean.
            assertInstanceOf(SmsNotificationService.class, manager.getNotificationService());
        }
    }
}
