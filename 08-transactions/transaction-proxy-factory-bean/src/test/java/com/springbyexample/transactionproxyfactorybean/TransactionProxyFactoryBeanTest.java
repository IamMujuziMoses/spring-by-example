package com.springbyexample.transactionproxyfactorybean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class TransactionProxyFactoryBeanTest {

    @Test
    void shouldSaveGreeting() {
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml")) {

            GreetingService greetingService = context.getBean("greetingService", GreetingService.class);

            greetingService.saveGreeting("Hello, Spring!");

            assertEquals(1, greetingService.countGreetings());
        }
    }

    @Test
    void shouldRollbackTransactionWhenTargetMethodFails() {
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml")) {

            GreetingService greetingService = context.getBean("greetingService", GreetingService.class);

            assertThrows(RuntimeException.class, () -> greetingService.saveGreetingAndFail("Hello, Spring!"));
            assertEquals(0, greetingService.countGreetings());
        }
    }

    @Test
    void shouldCreateTransactionProxy() {
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml")) {

            GreetingService greetingService = context.getBean("greetingService", GreetingService.class);

            assertTrue(AopUtils.isAopProxy(greetingService));
        }
    }
}
