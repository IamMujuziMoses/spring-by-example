package com.springbyexample.optionaldependencies;

import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class EmailPaymentNotificationService implements PaymentNotificationService {

    @Override
    public void notifyPayment() {
        System.out.println("Sending payment notification.");
    }
}
