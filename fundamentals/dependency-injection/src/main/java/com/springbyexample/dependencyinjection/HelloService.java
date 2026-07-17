package com.springbyexample.dependencyinjection;

/**
 * @author Mujuzi Moses
 */

public class HelloService {

    private final GreetingService greetingService;

    public HelloService(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public void sayHello() {
        System.out.println(greetingService.getGreeting());
    }
}