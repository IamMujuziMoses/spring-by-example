package com.springbyexample.programmatictransactions;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            System.out.println("=== Commit ===");

            greetingService.saveGreeting("Hello, Spring!");

            System.out.println("Greetings: " + greetingService.count());
            System.out.println();
            System.out.println("=== Explicit Rollback ===");

            greetingService.saveGreetingAndRollback("Hello, Rollback!");

            // setRollbackOnly() prevents the inserted row from being committed.
            System.out.println("Greetings: " + greetingService.count());
            System.out.println();
            System.out.println("=== Exception Rollback ===");

            try {
                greetingService.saveGreetingAndFail("Hello, Failure!");
            } catch (GreetingException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }

            // An exception also causes the transaction to roll back.
            System.out.println("Greetings: " + greetingService.count());
        }
    }
}
