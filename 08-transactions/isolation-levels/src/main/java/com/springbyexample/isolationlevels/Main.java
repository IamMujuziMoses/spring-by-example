package com.springbyexample.isolationlevels;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            AccountService accountService = context.getBean(AccountService.class);

            System.out.println("Initial balance: " + accountService.readWithReadCommitted(1L));

            accountService.updateBalance(1L, new java.math.BigDecimal("1500.00"));

            System.out.println("Updated balance: " + accountService.readWithReadCommitted(1L));
        }
    }
}
