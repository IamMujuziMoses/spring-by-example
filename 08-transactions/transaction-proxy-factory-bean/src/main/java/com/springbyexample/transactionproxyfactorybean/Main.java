package com.springbyexample.transactionproxyfactorybean;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml")) {

            GreetingService greetingService = context.getBean("greetingService", GreetingService.class);

            System.out.println("=== Successful Transaction ===");
            greetingService.saveGreeting("Hello, Spring!");

            System.out.println("Greetings: " + greetingService.countGreetings());
            System.out.println();
            System.out.println("=== Rolled Back Transaction ===");

            try {
                greetingService.saveGreetingAndFail("Hello, TransactionProxyFactoryBean!");
            } catch (RuntimeException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }

            System.out.println("Greetings: " + greetingService.countGreetings());
        }
    }
}
