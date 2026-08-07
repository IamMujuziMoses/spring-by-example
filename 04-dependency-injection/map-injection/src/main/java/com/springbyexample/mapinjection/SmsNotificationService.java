package com.springbyexample.mapinjection;

import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class SmsNotificationService implements NotificationService {

    @Override
    public String getName() {
        return "SMS";
    }

    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}
