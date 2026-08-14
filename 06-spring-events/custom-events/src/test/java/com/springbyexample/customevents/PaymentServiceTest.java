package com.springbyexample.customevents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class PaymentServiceTest {

    @Test
    void shouldNotifyWhenPaymentCompletedEventIsPublished() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var paymentService = context.getBean(PaymentService.class);
            var notificationService = context.getBean(PaymentNotificationService.class);
            var payment = new Payment(1001L, 250.00);

            paymentService.completePayment(payment);

            // Verify that the custom event was handled by the listener.
            assertEquals(1001L, notificationService.getLastNotifiedPaymentId());
        }
    }

    @Test
    void shouldNotNotifyBeforePaymentIsCompleted() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var notificationService = context.getBean(PaymentNotificationService.class);

            // No PaymentCompletedEvent has been published yet.
            assertNull(notificationService.getLastNotifiedPaymentId());
        }
    }

}
