package com.springbyexample.mapinjection;

import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
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
