package com.springbyexample.transactionpropagation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            System.out.println("=== REQUIRED ===");

            // Both operations share the same transaction.
            try {
                greetingService.saveWithRequiredAudit("Hello, Spring!");
            } catch (RuntimeException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }

            System.out.println("Greetings: " + greetingService.countGreetings());
            System.out.println("Audit entries: " + greetingService.countAuditEntries());
            System.out.println();
            System.out.println("=== REQUIRES_NEW ===");

            // The audit operation runs in its own transaction.
            try {
                greetingService.saveWithRequiresNewAudit("Hello, Spring!");
            } catch (RuntimeException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }

            System.out.println("Greetings: " + greetingService.countGreetings());
            System.out.println("Audit entries: " + greetingService.countAuditEntries());
        }
    }
}
