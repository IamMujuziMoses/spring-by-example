package com.springbyexample.componentscan;

import org.springframework.stereotype.Service;

/**
 * @author Mujuzi Moses
 */
@Service
public class GreetingService {

    private final GreetingRepository repository;

    public GreetingService(GreetingRepository repository) {
        this.repository = repository;
    }

    public String getGreeting() {
        return repository.getGreeting();
    }

}
