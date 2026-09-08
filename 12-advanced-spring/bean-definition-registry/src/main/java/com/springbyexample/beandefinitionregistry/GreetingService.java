package com.springbyexample.beandefinitionregistry;

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