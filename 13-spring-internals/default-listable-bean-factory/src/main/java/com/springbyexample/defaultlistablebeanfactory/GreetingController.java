package com.springbyexample.defaultlistablebeanfactory;

/**
 * @author Mujuzi Moses
 */
public record GreetingController(GreetingService greetingService) {

    public String greet() {
        return greetingService.greet();
    }
}
