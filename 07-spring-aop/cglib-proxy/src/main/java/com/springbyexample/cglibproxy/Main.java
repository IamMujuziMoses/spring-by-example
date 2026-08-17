package com.springbyexample.cglibproxy;

import org.springframework.cglib.proxy.Enhancer;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        var enhancer = new Enhancer();

        enhancer.setSuperclass(OrderService.class);
        enhancer.setCallback(new LoggingMethodInterceptor());

        OrderService orderService = (OrderService) enhancer.create();

        orderService.createOrder(1001L);
    }
}
