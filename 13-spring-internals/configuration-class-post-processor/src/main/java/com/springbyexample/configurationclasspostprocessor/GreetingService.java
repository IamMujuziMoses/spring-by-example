package com.springbyexample.configurationclasspostprocessor;

import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class GreetingService {

    public String greet() {
        return "Hello from GreetingService!";
    }
}