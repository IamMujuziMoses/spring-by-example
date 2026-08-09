package com.springbyexample.circulardependencies;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class AppConfigTest {

    @Test
    void shouldFailToCreateContextBecauseOfCircularDependency() {

        Exception exception = assertThrows(BeanCreationException.class, () -> new AnnotationConfigApplicationContext(AppConfig.class));

        assertTrue(exception.getMessage().contains("ServiceA") || exception.getMessage().contains("ServiceB"));
    }
}
