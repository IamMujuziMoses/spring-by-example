package com.springbyexample.controller;

import org.springframework.stereotype.Service;

/**
 * @author Mujuzi Moses
 */
@Service
public class GreetingService {

    public String getGreeting() {
        return "Hello from GreetingService!";
    }

}
