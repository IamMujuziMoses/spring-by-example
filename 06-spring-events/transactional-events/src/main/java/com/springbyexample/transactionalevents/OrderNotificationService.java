package com.springbyexample.transactionalevents;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author Mujuzi Moses
 */
@Component
public class OrderNotificationService {

    private Long lastNotifiedOrderId;

    @TransactionalEventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        lastNotifiedOrderId = event.getOrderId();

        System.out.println("Sending notification for order: " + event.getOrderId());
    }

    public Long getLastNotifiedOrderId() {
        return lastNotifiedOrderId;
    }
}
