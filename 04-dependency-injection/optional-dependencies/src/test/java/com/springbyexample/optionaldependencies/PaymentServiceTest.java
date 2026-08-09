package com.springbyexample.optionaldependencies;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class PaymentServiceTest {

    @Test
    void shouldInjectOptionalDependencyWhenBeanExists() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            PaymentService paymentService = context.getBean(PaymentService.class);

            assertTrue(paymentService.getNotificationService().isPresent());
            assertInstanceOf(EmailPaymentNotificationService.class, paymentService.getNotificationService().get());
        }
    }

    @Test
    void shouldInjectEmptyOptionalWhenBeanIsMissing() {

        try (var context = new AnnotationConfigApplicationContext(PaymentServiceOnlyConfig.class)) {

            PaymentService paymentService = context.getBean(PaymentService.class);

            assertTrue(paymentService.getNotificationService().isEmpty());
        }
    }
}
