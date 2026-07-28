package com.springbyexample.bean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);
            TimeService timeService = context.getBean(TimeService.class);
            System.out.println(greetingService.greet());
            System.out.println();
            System.out.println("Current time: " + timeService.currentTime());
        }
    }
}
