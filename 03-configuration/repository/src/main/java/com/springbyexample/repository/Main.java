package com.springbyexample.repository;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {
    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingRepository repository = context.getBean(GreetingRepository.class);

            System.out.println(repository.findGreeting());
        }
    }
}
