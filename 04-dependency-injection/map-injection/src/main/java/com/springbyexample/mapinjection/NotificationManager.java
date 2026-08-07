package com.springbyexample.mapinjection;

import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class NotificationManager {

    private final Map<String, NotificationService> notificationServices;

    public NotificationManager(Map<String, NotificationService> notificationServices) {

        this.notificationServices = notificationServices;
    }

    public void notify(String type, String message) {

        NotificationService service = notificationServices.get(type);

        if (service == null) {
            throw new IllegalArgumentException("Unknown notification type: " + type);
        }

        service.send(message);
    }

    public Map<String, NotificationService> getNotificationServices() {
        return notificationServices;
    }
}
