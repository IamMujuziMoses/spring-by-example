package com.springbyexample.dependencyinjection;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */

public class Main {
    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);

        HelloService service = context.getBean(HelloService.class);

        service.sayHello();
    }
}
