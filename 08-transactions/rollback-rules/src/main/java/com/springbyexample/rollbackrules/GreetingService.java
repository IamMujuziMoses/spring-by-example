package com.springbyexample.rollbackrules;

import com.springbyexample.rollbackrules.exceptions.CheckedGreetingException;
import com.springbyexample.rollbackrules.exceptions.UncheckedGreetingException;
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
    public void saveWithRuntimeException(String message) {
        greetingRepository.save(message);

        throw new UncheckedGreetingException("Something went wrong");
    }

    @Transactional
    public void saveWithCheckedException(String message) throws CheckedGreetingException {
        greetingRepository.save(message);

        throw new CheckedGreetingException("Something went wrong");
    }

    @Transactional(rollbackFor = CheckedGreetingException.class)
    public void saveWithRollbackFor(String message) throws CheckedGreetingException {
        greetingRepository.save(message);

        throw new CheckedGreetingException("Something went wrong");
    }

    @Transactional(noRollbackFor = UncheckedGreetingException.class)
    public void saveWithNoRollbackFor(String message) {
        greetingRepository.save(message);

        throw new UncheckedGreetingException("Something went wrong");
    }

    public int count() {
        return greetingRepository.count();
    }
}
