package com.springbyexample.environment;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @author Mujuzi Moses
 */
public class EnvironmentApplicationTest {

    @Test
    void shouldExposeEnvironment() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext()) {

            Environment environment = applicationContext.getEnvironment();

            assertSame(environment, applicationContext.getEnvironment());
        }
    }

    @Test
    void shouldSetAndRetrieveActiveProfiles() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext()) {

            applicationContext.getEnvironment().setActiveProfiles("development", "testing");

            assertArrayEquals(new String[] {"development", "testing"},
                    applicationContext.getEnvironment().getActiveProfiles());
        }
    }

    @Test
    void shouldExposeDefaultProfiles() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext()) {

            assertArrayEquals(new String[] {"default"},
                    applicationContext.getEnvironment().getDefaultProfiles());
        }
    }
}
