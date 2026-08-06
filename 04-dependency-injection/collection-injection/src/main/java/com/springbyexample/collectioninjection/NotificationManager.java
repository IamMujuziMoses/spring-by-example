package com.springbyexample.collectioninjection;

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

    public void notifyUsers() {

        notificationServices.forEach(service -> System.out.println(service.send()));
    }

    public List<NotificationService> getNotificationServices() {
        return notificationServices;
    }
}
