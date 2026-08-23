package com.springbyexample.programmatictransactions;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author Mujuzi Moses
 */
@Service
public class GreetingService {

    private final GreetingRepository greetingRepository;

    private final TransactionTemplate transactionTemplate;

    public GreetingService(GreetingRepository greetingRepository, TransactionTemplate transactionTemplate) {
        this.greetingRepository = greetingRepository;
        this.transactionTemplate = transactionTemplate;
    }

    public void saveGreeting(String message) {
        transactionTemplate.executeWithoutResult(status -> {greetingRepository.save(message);});
    }

    public void saveGreetingAndRollback(String message) {
        transactionTemplate.executeWithoutResult(status -> {greetingRepository.save(message);

            // Explicitly mark the transaction for rollback.
            status.setRollbackOnly();
        });
    }

    public void saveGreetingAndFail(String message) {
        transactionTemplate.executeWithoutResult(status -> {greetingRepository.save(message);

            throw new GreetingException("Something went wrong");});
    }

    public int count() {
        return greetingRepository.count();
    }
}
