package com.springbyexample.transactionalevents;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mujuzi Moses
 */
@Service
public class OrderService {

    private final ApplicationEventPublisher eventPublisher;

    public OrderService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void createOrder(Long orderId) {
        System.out.println("Creating order: " + orderId);

        eventPublisher.publishEvent(new OrderCreatedEvent(orderId));
    }
}
