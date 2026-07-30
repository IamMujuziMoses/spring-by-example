package com.springbyexample.service;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {
    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            GreetingService greetingService = context.getBean(GreetingService.class);

            System.out.println(greetingService.greet());
        }
    }
}
