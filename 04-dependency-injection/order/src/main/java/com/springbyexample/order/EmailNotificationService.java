package com.springbyexample.order;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
@Order(3)
public class EmailNotificationService implements NotificationService {

    @Override
    public String getName() {
        return "Email";
    }
    @Override
    public void send(String message) {
        System.out.println("Sending email notification: " + message);
    }
}
