package com.springbyexample.xmltojavaconfiguration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class XmlToJavaConfigurationApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class)) {

            GreetingService xmlGreetingService = context.getBean("greetingService", GreetingService.class);
            GreetingService javaGreetingService = context.getBean("javaGreetingService", GreetingService.class);

            System.out.println("XML service: " + xmlGreetingService.greet());
            System.out.println("Java service: " + javaGreetingService.greet());
        }
    }
}
