package com.springbyexample.servicecontext.impl;

import com.springbyexample.servicecontext.GreetingService;

/**
 * @author Mujuzi Moses
 */
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greet() {
        return "Hello from the OpenMRS ServiceContext!";
    }
}
