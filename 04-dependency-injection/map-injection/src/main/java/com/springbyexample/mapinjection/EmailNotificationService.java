package com.springbyexample.mapinjection;

import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class EmailNotificationService implements NotificationService {

    @Override
    public String getName() {
        return "Email";
    }

    @Override
    public void send(String message) {
        System.out.println("Sending email: " + message);
    }
}
