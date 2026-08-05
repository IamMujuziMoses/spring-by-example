package com.springbyexample.javaconfig;

/**
 * @author Mujuzi Moses
 */
public class GreetingController {

    private final GreetingService service;

    public GreetingController(GreetingService service) {
        this.service = service;
    }

    public String greet() {
        return service.getGreeting();
    }

}
