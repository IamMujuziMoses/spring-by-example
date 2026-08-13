package com.springbyexample.listeningforevents;

import org.springframework.context.ApplicationEventPublisher;

/**
 * @author Mujuzi Moses
 */
public class OrderService {

    private final ApplicationEventPublisher eventPublisher;

    public OrderService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void createOrder(Long orderId) {

        System.out.println("Creating order: " + orderId);

        // Publish an event after the order has been created.
        eventPublisher.publishEvent(new OrderCreatedEvent(orderId));
    }
}
