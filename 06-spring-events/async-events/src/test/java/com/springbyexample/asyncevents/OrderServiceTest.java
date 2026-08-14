package com.springbyexample.asyncevents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class OrderServiceTest {

    @Test
    void shouldHandleOrderCreatedEventAsynchronously() throws InterruptedException {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var orderService = context.getBean(OrderService.class);
            var notificationService = context.getBean(OrderNotificationService.class);

            orderService.createOrder(1001L);

            assertTrue(notificationService.awaitNotification());

            assertEquals(1001L, notificationService.getLastNotifiedOrderId());
        }
    }
}
