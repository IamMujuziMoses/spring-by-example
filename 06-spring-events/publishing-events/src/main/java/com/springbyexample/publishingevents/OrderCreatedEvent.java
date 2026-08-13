package com.springbyexample.publishingevents;

/**
 * @author Mujuzi Moses
 */
public class OrderCreatedEvent {

    private final Long orderId;

    public OrderCreatedEvent(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}