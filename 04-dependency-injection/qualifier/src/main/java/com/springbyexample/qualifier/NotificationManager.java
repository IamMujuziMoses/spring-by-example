package com.springbyexample.qualifier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class NotificationManager {

    private final NotificationService notificationService;

    // @Qualifier tells Spring exactly which NotificationService implementation should be injected.
    public NotificationManager(@Qualifier("smsNotificationService") NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void notifyUser() {
        System.out.println(notificationService.send());
    }
}
