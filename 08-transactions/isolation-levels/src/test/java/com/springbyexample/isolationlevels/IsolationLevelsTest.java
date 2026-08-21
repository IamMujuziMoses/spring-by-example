package com.springbyexample.isolationlevels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.aop.support.AopUtils.isAopProxy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class IsolationLevelsTest {

    @Test
    void shouldReadAccountBalance() {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            AccountService accountService = context.getBean(AccountService.class);

            assertEquals(new BigDecimal("1000.00"), accountService.readWithReadCommitted(1L));
        }
    }

    @Test
    void shouldUpdateAccountBalance() {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            AccountService accountService = context.getBean(AccountService.class);
            accountService.updateBalance(1L, new BigDecimal("1500.00"));

            assertEquals(new BigDecimal("1500.00"), accountService.readWithReadCommitted(1L));
        }
    }

    @Test
    void shouldCreateTransactionalProxy() {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            AccountService accountService = context.getBean(AccountService.class);

            assertTrue(isAopProxy(accountService));
        }
    }
}
