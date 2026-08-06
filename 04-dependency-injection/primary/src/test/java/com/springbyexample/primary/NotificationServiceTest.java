package com.springbyexample.primary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class NotificationServiceTest {

    @Test
    void shouldRegisterAllNotificationServiceBeans() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            Map<String, NotificationService> services = context.getBeansOfType(NotificationService.class);

            // @Primary does not remove or replace other beans, both implementations remain
            // registered in the ApplicationContext.
            assertEquals(2, services.size());
            assertTrue(services.containsKey("emailNotificationService"));
            assertTrue(services.containsKey("smsNotificationService"));
        }
    }

    @Test
    void shouldReturnPrimaryBeanWhenLookingUpByType() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            NotificationService service = context.getBean(NotificationService.class);

            // Looking up a bean by its interface type returns the implementation marked with @Primary.
            assertInstanceOf(EmailNotificationService.class, service);
        }
    }
}
