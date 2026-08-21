package com.springbyexample.rollbackrules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.springbyexample.rollbackrules.exceptions.CheckedGreetingException;
import com.springbyexample.rollbackrules.exceptions.UncheckedGreetingException;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class RollbackRulesTest {

    @Test
    void shouldRollbackForRuntimeException() {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            GreetingService greetingService = context.getBean(GreetingService.class);

            assertThrows(UncheckedGreetingException.class, () -> greetingService.saveWithRuntimeException("Runtime greeting"));

            // Runtime exceptions roll back by default.
            assertEquals(0, greetingService.count());
        }
    }

    @Test
    void shouldCommitForCheckedExceptionByDefault() throws Exception {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            GreetingService greetingService = context.getBean(GreetingService.class);

            assertThrows(CheckedGreetingException.class, () -> greetingService.saveWithCheckedException("Checked greeting"));

            // Checked exceptions do not roll back by default.
            assertEquals(1, greetingService.count());
        }
    }

    @Test
    void shouldRollbackCheckedExceptionWithRollbackFor() throws Exception {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            GreetingService greetingService = context.getBean(GreetingService.class);

            assertThrows(CheckedGreetingException.class, () -> greetingService.saveWithRollbackFor("Rollback greeting"));

            // rollbackFor explicitly requests a rollback.
            assertEquals(0, greetingService.count());
        }
    }

    @Test
    void shouldCommitRuntimeExceptionWithNoRollbackFor() {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            GreetingService greetingService = context.getBean(GreetingService.class);

            assertThrows(UncheckedGreetingException.class, () -> greetingService.saveWithNoRollbackFor("No rollback greeting"));

            // noRollbackFor overrides the default rollback behavior.
            assertEquals(1, greetingService.count());
        }
    }
}
