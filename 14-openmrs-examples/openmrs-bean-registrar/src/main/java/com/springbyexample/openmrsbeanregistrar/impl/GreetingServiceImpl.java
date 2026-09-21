package com.springbyexample.openmrsbeanregistrar.impl;

import com.springbyexample.openmrsbeanregistrar.GreetingService;

/**
 * @author Mujuzi Moses
 */
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greet() {
        return "Hello from an OpenMRS registered component!";
    }
}
