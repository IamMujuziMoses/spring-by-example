package com.springbyexample.listeningforevents;

import org.springframework.context.event.EventListener;

/**
 * @author Mujuzi Moses
 */
public class OrderNotificationService {

    private Long lastNotifiedOrderId;

    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        lastNotifiedOrderId = event.getOrderId();

        System.out.println("Sending notification for order: " + event.getOrderId());
    }

    public Long getLastNotifiedOrderId() {
        return lastNotifiedOrderId;
    }
}
