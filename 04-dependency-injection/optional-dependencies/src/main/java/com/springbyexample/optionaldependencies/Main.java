package com.springbyexample.optionaldependencies;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            PaymentService paymentService = context.getBean(PaymentService.class);

            paymentService.processPayment();
        }
    }
}
