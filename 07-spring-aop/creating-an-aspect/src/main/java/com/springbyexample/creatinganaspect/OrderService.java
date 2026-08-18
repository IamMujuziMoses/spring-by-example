package com.springbyexample.creatinganaspect;

import org.springframework.stereotype.Service;

/**
 * @author Mujuzi Moses
 */
@Service
public class OrderService {

    public void createOrder(Long orderId) {
        System.out.println("Creating order: " + orderId);
    }
}
