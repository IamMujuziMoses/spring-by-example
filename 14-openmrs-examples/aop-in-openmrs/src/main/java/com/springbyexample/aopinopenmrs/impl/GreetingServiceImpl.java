package com.springbyexample.aopinopenmrs.impl;

import com.springbyexample.aopinopenmrs.GreetingService;

/**
 * @author Mujuzi Moses
 */
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greet() {
        return "Hello from an OpenMRS AOP service!";
    }
}
