package com.springbyexample.listeningforevents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class OrderServiceTest {

    @Test
    void shouldNotifyWhenOrderCreatedEventIsPublished() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var orderService = context.getBean(OrderService.class);
            var notificationService = context.getBean(OrderNotificationService.class);

            orderService.createOrder(1001L);

            // Verify that the listener handled the event for the created order.
            assertEquals(1001L, notificationService.getLastNotifiedOrderId());
        }
    }

    @Test
    void shouldNotNotifyBeforeOrderCreatedEventIsPublished() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var notificationService = context.getBean(OrderNotificationService.class);

            // No event has been published yet, so the listener should not have run.
            assertNull(notificationService.getLastNotifiedOrderId());
        }
    }

}
