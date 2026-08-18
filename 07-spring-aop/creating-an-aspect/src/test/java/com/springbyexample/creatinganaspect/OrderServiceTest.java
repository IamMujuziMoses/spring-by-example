package com.springbyexample.creatinganaspect;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class OrderServiceTest {

    @Test
    void shouldInvokeAspectBeforeCreatingOrder() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var orderService = context.getBean(OrderService.class);
            var aspect = context.getBean(OrderLoggingAspect.class);

            assertFalse(aspect.wasInvoked());

            orderService.createOrder(1001L);

            assertTrue(aspect.wasInvoked());
        }
    }
}
