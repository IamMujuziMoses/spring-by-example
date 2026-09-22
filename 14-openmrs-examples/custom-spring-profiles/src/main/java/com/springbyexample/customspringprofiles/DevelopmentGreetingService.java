package com.springbyexample.customspringprofiles;

/**
 * @author Mujuzi Moses
 */
public class DevelopmentGreetingService implements GreetingService {

    @Override
    public String greet() {
        return "Hello from the development environment!";
    }
}