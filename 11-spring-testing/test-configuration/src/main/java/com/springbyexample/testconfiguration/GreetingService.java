package com.springbyexample.testconfiguration;

import org.springframework.stereotype.Service;

/**
 * @author Mujuzi Moses
 */
@Service
public class GreetingService {

    private final GreetingProvider greetingProvider;

    public GreetingService(GreetingProvider greetingProvider) {
        this.greetingProvider = greetingProvider;
    }

    public String greet() {
        return greetingProvider.getGreeting();
    }
}
