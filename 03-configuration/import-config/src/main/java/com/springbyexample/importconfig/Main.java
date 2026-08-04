package com.springbyexample.importconfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {
    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greeting = context.getBean(GreetingService.class);

            TimeService time = context.getBean(TimeService.class);

            System.out.println(greeting.greet());
            System.out.println(time.getTimeZone());
        }
    }
}
