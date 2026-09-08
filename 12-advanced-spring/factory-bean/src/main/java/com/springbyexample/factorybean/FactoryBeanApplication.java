package com.springbyexample.factorybean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class FactoryBeanApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext(FactoryBeanConfig.class)) {

            GreetingService greetingService = applicationContext.getBean("greetingService", GreetingService.class);

            System.out.println(greetingService.greet());
        }
    }
}
