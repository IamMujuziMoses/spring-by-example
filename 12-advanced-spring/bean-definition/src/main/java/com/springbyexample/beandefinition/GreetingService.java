package com.springbyexample.beandefinition;

/**
 * @author Mujuzi Moses
 */
public class GreetingService {

    private final String message;

    public GreetingService(String message) {
        this.message = message;
    }

    public String greet() {
        return message;
    }
}
