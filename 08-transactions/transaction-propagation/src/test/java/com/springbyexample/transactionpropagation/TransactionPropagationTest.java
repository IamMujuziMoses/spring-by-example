package com.springbyexample.transactionpropagation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class TransactionPropagationTest {

    @Test
    void shouldRollbackBothOperationsWithRequiredPropagation() {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            // Both operations roll back when the outer transaction fails.
            assertThrows(RuntimeException.class, () -> greetingService.saveWithRequiredAudit("Hello, Spring!"));
            assertEquals(0, greetingService.countGreetings());
            assertEquals(0, greetingService.countAuditEntries());
        }
    }

    @Test
    void shouldCommitIndependentTransactionWithRequiresNewPropagation() {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            // The audit transaction commits even though the outer transaction fails.
            assertThrows(RuntimeException.class, () -> greetingService.saveWithRequiresNewAudit("Hello, Spring!"));
            assertEquals(0, greetingService.countGreetings());
            assertEquals(1, greetingService.countAuditEntries());
        }
    }
}
