package com.springbyexample.whatisaop;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class OrderServiceTest {

    @Test
    void shouldCreateOrder() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var orderService = context.getBean(OrderService.class);

            assertDoesNotThrow(() -> orderService.createOrder(1001L));
        }
    }
}
