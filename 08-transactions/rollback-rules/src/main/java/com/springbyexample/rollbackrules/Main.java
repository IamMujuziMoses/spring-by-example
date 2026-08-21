package com.springbyexample.rollbackrules;

import com.springbyexample.rollbackrules.exceptions.CheckedGreetingException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            GreetingService greetingService = context.getBean(GreetingService.class);

            System.out.println("=== Runtime Exception ===");

            try {
                greetingService.saveWithRuntimeException("Runtime greeting");
            } catch (RuntimeException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }

            // Runtime exceptions trigger rollback by default.
            System.out.println("Greetings: " + greetingService.count());
            System.out.println();
            System.out.println("=== Checked Exception ===");

            try {
                greetingService.saveWithCheckedException("Checked greeting");
            } catch (CheckedGreetingException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }

            // Checked exceptions do not trigger rollback by default.
            System.out.println("Greetings: " + greetingService.count());
            System.out.println();
            System.out.println("=== rollbackFor ===");

            try {
                greetingService.saveWithRollbackFor("Rollback greeting");
            } catch (CheckedGreetingException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }

            // rollbackFor explicitly overrides the default behavior.
            System.out.println("Greetings: " + greetingService.count());
            System.out.println();
            System.out.println("=== noRollbackFor ===");

            try {
                greetingService.saveWithNoRollbackFor("No rollback greeting");
            } catch (RuntimeException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }

            // noRollbackFor prevents rollback for the specified exception.
            System.out.println("Greetings: " + greetingService.count());
        }
    }
}
