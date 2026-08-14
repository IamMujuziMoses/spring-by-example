package com.springbyexample.transactionalevents;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class OrderServiceTest {

    @Test
    void shouldHandleEventAfterTransactionCommits() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var orderService = context.getBean(OrderService.class);
            var notificationService = context.getBean(OrderNotificationService.class);

            orderService.createOrder(1001L);

            assertEquals(1001L, notificationService.getLastNotifiedOrderId());
        }
    }
}
