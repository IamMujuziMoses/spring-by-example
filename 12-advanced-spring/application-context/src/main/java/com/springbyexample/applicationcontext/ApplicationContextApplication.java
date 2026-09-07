package com.springbyexample.applicationcontext;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class ApplicationContextApplication {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(ApplicationContextConfig.class)) {

            GreetingService greetingService = applicationContext.getBean(GreetingService.class);

            System.out.println(greetingService.greet());
        }
    }
}
