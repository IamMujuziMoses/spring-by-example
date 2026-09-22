package com.springbyexample.customspringprofiles;

/**
 * @author Mujuzi Moses
 */
public class ProductionGreetingService implements GreetingService {

    @Override
    public String greet() {
        return "Hello from the production environment!";
    }
}
