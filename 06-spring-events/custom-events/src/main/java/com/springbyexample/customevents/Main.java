package com.springbyexample.customevents;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var paymentService = context.getBean(PaymentService.class);
            var payment = new Payment(1001L, 250.00);

            paymentService.completePayment(payment);
        }
    }
}
