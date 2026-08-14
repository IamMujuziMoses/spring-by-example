package com.springbyexample.customevents;

import org.springframework.context.event.EventListener;

/**
 * @author Mujuzi Moses
 */
public class PaymentNotificationService {

    private Long lastNotifiedPaymentId;

    @EventListener
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        lastNotifiedPaymentId = event.getPayment().getId();

        System.out.println("Sending payment notification for: " + event.getPayment().getId());
    }

    public Long getLastNotifiedPaymentId() {
        return lastNotifiedPaymentId;
    }
}
