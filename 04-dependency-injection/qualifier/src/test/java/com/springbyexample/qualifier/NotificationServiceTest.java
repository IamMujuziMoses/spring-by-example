package com.springbyexample.qualifier;

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

            // Retrieve all NotificationService beans registered in the ApplicationContext.
            Map<String, NotificationService> services = context.getBeansOfType(NotificationService.class);

            // Using @Qualifier does not affect bean registration. Both implementations remain available in the container.
            assertEquals(2, services.size());
            assertTrue(services.containsKey("emailNotificationService"));
            assertTrue(services.containsKey("smsNotificationService"));
        }
    }

    @Test
    void shouldRetrieveBeanByName() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            // Retrieve the bean using its Spring bean name.
            NotificationService service = context.getBean("smsNotificationService", NotificationService.class);

            // The bean name matches the value supplied to @Qualifier, allowing Spring to resolve the specific implementation.
            assertInstanceOf(SmsNotificationService.class, service);
        }
    }
}
