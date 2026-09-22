package com.springbyexample.transactionmanagement.impl;

import com.springbyexample.transactionmanagement.GreetingService;

/**
 * @author Mujuzi Moses
 */
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String saveGreeting() {
        return "Greeting saved successfully!";
    }

    @Override
    public String fail() {
        throw new IllegalStateException("Something went wrong!");
    }
}
