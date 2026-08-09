package com.springbyexample.optionaldependencies;

import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * @author Mujuzi Moses
 */
@Service
public class PaymentService {

    private final Optional<PaymentNotificationService> notificationService;

    public PaymentService(Optional<PaymentNotificationService> notificationService) {

        this.notificationService = notificationService;
    }

    public void processPayment() {

        System.out.println("Payment processed successfully.");
        notificationService.ifPresent(PaymentNotificationService::notifyPayment);
    }

    public Optional<PaymentNotificationService> getNotificationService() {
        return notificationService;
    }
}
