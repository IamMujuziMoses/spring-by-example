package com.springbyexample.jdkdynamicproxy;

/**
 * @author Mujuzi Moses
 */
public class OrderServiceImpl implements OrderService {

    @Override
    public void createOrder(Long orderId) {
        System.out.println("Creating order: " + orderId);
    }
}