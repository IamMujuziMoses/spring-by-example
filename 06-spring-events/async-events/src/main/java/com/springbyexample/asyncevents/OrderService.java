package com.springbyexample.asyncevents;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @author Mujuzi Moses
 */
@Service
public class OrderService {

    private final ApplicationEventPublisher eventPublisher;

    public OrderService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void createOrder(Long orderId) {
        System.out.println("Creating order: " + orderId);

        eventPublisher.publishEvent(new OrderCreatedEvent(orderId));

        System.out.println("Order creation completed.");
    }
}
