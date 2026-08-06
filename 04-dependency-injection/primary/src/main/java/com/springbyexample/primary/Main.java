package com.springbyexample.primary;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            NotificationManager manager = context.getBean(NotificationManager.class);

            manager.notifyUser();
        }
    }
}
