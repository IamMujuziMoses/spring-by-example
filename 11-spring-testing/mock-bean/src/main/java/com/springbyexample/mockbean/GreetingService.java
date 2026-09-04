package com.springbyexample.mockbean;

import org.springframework.stereotype.Service;

/**
 * @author Mujuzi Moses
 */
@Service
public class GreetingService {

    private final GreetingRepository greetingRepository;

    public GreetingService(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    public String greet(Long id) {
        String name = greetingRepository.findNameById(id);
        return "Hello, " + name + "!";
    }
}
