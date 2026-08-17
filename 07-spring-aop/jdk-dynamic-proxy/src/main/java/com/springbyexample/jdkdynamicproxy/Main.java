package com.springbyexample.jdkdynamicproxy;

import java.lang.reflect.Proxy;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        var target = new OrderServiceImpl();

        var handler = new LoggingInvocationHandler(target);

        OrderService orderService = (OrderService) Proxy.newProxyInstance(OrderService.class.getClassLoader(),
                new Class<?>[]{OrderService.class}, handler);

        orderService.createOrder(1001L);
    }
}
