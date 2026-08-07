package com.springbyexample.order;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
@Order(1)
public class SmsNotificationService implements NotificationService {

    @Override
    public String getName() {
        return "SMS";
    }
    @Override
    public void send(String message) {
        System.out.println("Sending SMS notification: " + message);
    }
}
