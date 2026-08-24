package com.springbyexample.transactionproxyfactorybean;

/**
 * @author Mujuzi Moses
 */
public class GreetingService {

    private final GreetingRepository greetingRepository;

    public GreetingService(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    public void saveGreeting(String message) {
        greetingRepository.save(message);
    }

    public void saveGreetingAndFail(String message) {
        greetingRepository.save(message);

        throw new RuntimeException("Something went wrong");
    }

    public int countGreetings() {
        return greetingRepository.count();
    }
}
