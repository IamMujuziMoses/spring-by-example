package com.springbyexample.cglibproxy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @author Mujuzi Moses
 */
public class OrderServiceTest {

    @Test
    void shouldCreateCglibProxy() {
        var enhancer = new Enhancer();

        enhancer.setSuperclass(OrderService.class);
        enhancer.setCallback(new LoggingMethodInterceptor());

        OrderService orderService = (OrderService) enhancer.create();

        assertNotNull(orderService);
        assertInstanceOf(OrderService.class, orderService);
        assertSame(OrderService.class, orderService.getClass().getSuperclass());
    }

    @Test
    void shouldInvokeInterceptorWhenMethodIsCalled() {
        var interceptor = new LoggingMethodInterceptor();

        var enhancer = new Enhancer();
        enhancer.setSuperclass(OrderService.class);
        enhancer.setCallback(interceptor);

        OrderService orderService = (OrderService) enhancer.create();

        assertFalse(interceptor.wasInvoked());

        orderService.createOrder(1001L);

        assertTrue(interceptor.wasInvoked());
    }

}
