package com.springbyexample.cglibproxy;

/**
 * @author Mujuzi Moses
 */
public class OrderService {

    public void createOrder(Long orderId) {
        System.out.println("Creating order: " + orderId);
    }
}
