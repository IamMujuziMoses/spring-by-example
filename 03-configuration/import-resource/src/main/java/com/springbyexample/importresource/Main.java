package com.springbyexample.importresource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService service = context.getBean(GreetingService.class);

            System.out.println(service.greet());
        }
    }
}
