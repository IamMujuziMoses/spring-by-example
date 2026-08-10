package com.springbyexample.optionaldependencies;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */
@Configuration
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class PaymentServiceOnlyConfig {

    @Bean
    PaymentService paymentService(Optional<PaymentNotificationService> notificationService) {

        return new PaymentService(notificationService);
    }
}
