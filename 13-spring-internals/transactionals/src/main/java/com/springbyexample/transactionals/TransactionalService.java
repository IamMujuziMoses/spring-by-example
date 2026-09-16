package com.springbyexample.transactionals;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mujuzi Moses
 */
public class TransactionalService {

    private final GreetingService greetingService;

    public TransactionalService(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @Transactional
    public String greet() {
        return greetingService.greet();
    }

    @Transactional
    public String fail() {
        throw new IllegalStateException("Something went wrong!");
    }
}
