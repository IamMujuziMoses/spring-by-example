package com.springbyexample.transactional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mujuzi Moses
 */
@Service
public class GreetingService {

    private final GreetingRepository greetingRepository;

    public GreetingService(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    @Transactional
    public void saveGreeting(String message) {
        greetingRepository.save(message);
    }

    @Transactional
    public void saveGreetingAndFail(String message) {
        greetingRepository.save(message);

        throw new RuntimeException("Something went wrong");
    }
}