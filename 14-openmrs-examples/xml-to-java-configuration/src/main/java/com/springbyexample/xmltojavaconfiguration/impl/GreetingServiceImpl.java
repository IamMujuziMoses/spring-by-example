package com.springbyexample.xmltojavaconfiguration.impl;

import com.springbyexample.xmltojavaconfiguration.GreetingService;

/**
 * @author Mujuzi Moses
 */
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greet() {
        return "Hello from a configured service!";
    }
}
