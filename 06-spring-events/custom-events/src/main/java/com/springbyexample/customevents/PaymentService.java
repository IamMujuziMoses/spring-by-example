package com.springbyexample.customevents;

import org.springframework.context.ApplicationEventPublisher;

/**
 * @author Mujuzi Moses
 */
public class PaymentService {

    private final ApplicationEventPublisher eventPublisher;

    public PaymentService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void completePayment(Payment payment) {

        System.out.println("Payment completed: " + payment.getId());

        // Publish an application-specific event after the payment completes.
        eventPublisher.publishEvent(new PaymentCompletedEvent(payment));
    }
}
