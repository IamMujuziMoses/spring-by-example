package com.springbyexample.customevents;

/**
 * @author Mujuzi Moses
 */
public class PaymentCompletedEvent {

    private final Payment payment;

    public PaymentCompletedEvent(Payment payment) {
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }
}
