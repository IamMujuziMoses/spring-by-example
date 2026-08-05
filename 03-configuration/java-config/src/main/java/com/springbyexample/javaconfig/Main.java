package com.springbyexample.javaconfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingController controller = context.getBean(GreetingController.class);

            System.out.println(controller.greet());
        }
    }
}
