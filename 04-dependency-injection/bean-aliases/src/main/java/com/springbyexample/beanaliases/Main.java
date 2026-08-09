package com.springbyexample.beanaliases;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            NotificationService service = context.getBean("notificationService", NotificationService.class);
            NotificationService emailService = context.getBean("emailNotificationService", NotificationService.class);

            service.sendNotification();
            emailService.sendNotification();
        }
    }
}
