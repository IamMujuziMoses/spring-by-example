package com.springbyexample.aopproxies;

/**
 * @author Mujuzi Moses
 */
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greet() {
        return "Hello from GreetingService!";
    }
}
