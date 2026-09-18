package com.springbyexample.configurationclasspostprocessor;

/**
 * @author Mujuzi Moses
 */
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public String greet() {
        return greetingService.greet();
    }
}
