package com.springbyexample.jdkdynamicproxy;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Proxy;

import org.junit.jupiter.api.Test;

/**
 * @author Mujuzi Moses
 */
public class OrderServiceTest {

    @Test
    void shouldCreateJdkDynamicProxy() {

        var target = new OrderServiceImpl();
        var handler = new LoggingInvocationHandler(target);

        OrderService orderService = (OrderService) Proxy.newProxyInstance(OrderService.class.getClassLoader(),
                new Class<?>[]{OrderService.class}, handler);

        assertNotNull(orderService);
        assertInstanceOf(Proxy.class, orderService);
    }

    @Test
    void shouldInvokeTargetMethodThroughProxy() {

        var target = new OrderServiceImpl();
        var handler = new LoggingInvocationHandler(target);

        OrderService orderService = (OrderService) Proxy.newProxyInstance(OrderService.class.getClassLoader(),
                new Class<?>[]{OrderService.class}, handler);

        orderService.createOrder(1001L);
    }
}
