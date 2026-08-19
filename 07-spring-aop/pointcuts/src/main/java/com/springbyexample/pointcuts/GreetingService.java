package com.springbyexample.pointcuts;

/**
 * @author Mujuzi Moses
 */
public class GreetingService {

    public String greet(String name) {
        return "Hello, " + name + "!";
    }

    public String farewell(String name) {
        return "Goodbye, " + name + "!";
    }
}
