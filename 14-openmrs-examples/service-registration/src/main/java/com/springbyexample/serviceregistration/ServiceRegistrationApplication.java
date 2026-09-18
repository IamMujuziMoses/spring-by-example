package com.springbyexample.serviceregistration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Mujuzi Moses
 */
@ComponentScan("com.springbyexample.serviceregistration")
public class ServiceRegistrationApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(ServiceRegistrationApplication.class)) {

            GreetingService greetingService = context.getBean("greetingService", GreetingService.class);

            System.out.println(greetingService.greet());
        }
    }
}