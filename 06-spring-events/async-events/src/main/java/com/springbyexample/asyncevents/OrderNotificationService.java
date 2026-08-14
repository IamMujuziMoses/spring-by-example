package com.springbyexample.asyncevents;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class OrderNotificationService {

    private final CountDownLatch notificationLatch = new CountDownLatch(1);

    private Long lastNotifiedOrderId;

    @Async
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println("Sending notification for order: " + event.getOrderId());

        lastNotifiedOrderId = event.getOrderId();
        notificationLatch.countDown();
    }

    public boolean awaitNotification() throws InterruptedException {
        return notificationLatch.await(2, TimeUnit.SECONDS);
    }

    public Long getLastNotifiedOrderId() {
        return lastNotifiedOrderId;
    }
}
