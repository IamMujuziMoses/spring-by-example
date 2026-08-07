package com.springbyexample.order;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
@Order(2)
public class PushNotificationService implements NotificationService {

    @Override
    public String getName() {
        return "Push";
    }
    @Override
    public void send(String message) {
        System.out.println("Sending push notification: " + message);
    }
}
