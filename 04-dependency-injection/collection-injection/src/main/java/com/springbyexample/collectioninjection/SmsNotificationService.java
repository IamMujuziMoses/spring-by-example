package com.springbyexample.collectioninjection;

import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class SmsNotificationService implements NotificationService {

    @Override
    public String send() {
        return "Sending SMS notification";
    }
}
