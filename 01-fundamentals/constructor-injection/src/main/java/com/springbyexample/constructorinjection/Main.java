package com.springbyexample.constructorinjection;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */

public class Main {
    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        NotificationService service = context.getBean(NotificationService.class);
        service.notify("Welcome to Spring!");
    }
}
