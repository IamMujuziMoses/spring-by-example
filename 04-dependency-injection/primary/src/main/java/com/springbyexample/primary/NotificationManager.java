package com.springbyexample.primary;

import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class NotificationManager {

    private final NotificationService notificationService;

    public NotificationManager(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void notifyUser() {
        System.out.println(notificationService.send());
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

}
