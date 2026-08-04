package com.springbyexample.xmlconfig;

/**
 * @author Mujuzi Moses
 */
public class GreetingService {

    private final GreetingRepository repository;

    public GreetingService(GreetingRepository repository) {
        this.repository = repository;
    }

    public String getGreeting() {
        return repository.getGreeting();
    }

}
