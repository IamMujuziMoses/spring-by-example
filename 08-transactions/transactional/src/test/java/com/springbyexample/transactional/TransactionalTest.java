package com.springbyexample.transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class TransactionalTest {

    @Test
    void shouldCreateTransactionProxy() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            assertTrue(AopUtils.isAopProxy(greetingService));
        }
    }

    @Test
    void shouldSaveGreeting() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);
            GreetingRepository repository = context.getBean(GreetingRepository.class);

            greetingService.saveGreeting("Hello, Spring!");

            assertEquals(1, repository.count());
        }
    }

    @Test
    void shouldRollbackWhenRuntimeExceptionOccurs() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);
            GreetingRepository repository = context.getBean(GreetingRepository.class);

            assertThrows(RuntimeException.class, () -> greetingService.saveGreetingAndFail("Hello, Spring!"));

            assertEquals(0, repository.count());
        }
    }

}
