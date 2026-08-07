package com.springbyexample.order;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component

public class NotificationManager {

    private final List<NotificationService> notificationServices;

    public NotificationManager(List<NotificationService> notificationServices) {
        this.notificationServices = notificationServices;
    }

    public void notifyUsers(String message) {
        notificationServices.forEach(service -> service.send(message));
    }

    public List<NotificationService> getNotificationServices() {
        return notificationServices;
    }

}
