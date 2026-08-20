package com.springbyexample.adviceordering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class AdviceOrderingTest {

    @Test
    void shouldReturnGreeting() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            assertEquals("Hello, Spring!", greetingService.greet("Spring"));
        }
    }

    @Test
    void shouldCreateAopProxy() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            assertTrue(AopUtils.isAopProxy(greetingService));
        }
    }

    @Test
    void shouldExecuteAdviceInOrder() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            GreetingService greetingService = context.getBean(GreetingService.class);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));

                String result = greetingService.greet("Spring");

                assertEquals("Hello, Spring!", result);
            }
            finally {
                System.setOut(originalOut);
            }

            assertEquals("Logging: Before greeting" + System.lineSeparator()
                            + "Security: Checking access" + System.lineSeparator()
                            + "Security: After greeting" + System.lineSeparator()
                            + "Logging: After greeting" + System.lineSeparator(), output.toString());
        }
    }
}
