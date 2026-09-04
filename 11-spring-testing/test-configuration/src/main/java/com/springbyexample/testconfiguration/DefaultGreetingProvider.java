package com.springbyexample.testconfiguration;

import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class DefaultGreetingProvider implements GreetingProvider {

    @Override
    public String getGreeting() {
        return "Hello from production!";
    }
}
