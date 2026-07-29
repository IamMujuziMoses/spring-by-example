package com.springbyexample.service;

import org.springframework.stereotype.Service;

/**
 * @author Mujuzi Moses
 */
@Service
public class GreetingService {

    public String greet() {
        return "Hello from GreetingService!";
    }

}
