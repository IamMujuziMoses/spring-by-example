package com.springbyexample.programmatictransactions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class ProgrammaticTransactionsTest {

    @Test
    void shouldCommitSuccessfulTransaction() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);
            greetingService.saveGreeting("Hello, Spring!");

            assertEquals(1, greetingService.count());
        }
    }

    @Test
    void shouldRollbackWhenTransactionIsMarkedRollbackOnly() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            greetingService.saveGreetingAndRollback("Hello, Rollback!");

            // The transaction was explicitly marked for rollback.
            assertEquals(0, greetingService.count());
        }
    }

    @Test
    void shouldRollbackWhenExceptionIsThrown() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            assertThrows(GreetingException.class, () -> greetingService.saveGreetingAndFail("Hello, Failure!"));

            // The failed transaction does not commit its changes.
            assertEquals(0, greetingService.count());
        }
    }
}
