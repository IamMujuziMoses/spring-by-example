package com.springbyexample.importselector;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class ImportSelectorApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(ApplicationConfig.class)) {

            GreetingService greetingService = applicationContext.getBean(GreetingService.class);

            System.out.println(greetingService.greet());
        }
    }
}