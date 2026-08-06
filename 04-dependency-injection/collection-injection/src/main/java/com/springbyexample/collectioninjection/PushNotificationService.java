package com.springbyexample.collectioninjection;

import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class PushNotificationService implements NotificationService {

    @Override
    public String send() {
        return "Sending push notification";
    }
}
