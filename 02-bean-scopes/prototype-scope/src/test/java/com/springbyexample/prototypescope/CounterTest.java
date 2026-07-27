package com.springbyexample.prototypescope;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class CounterTest {

    @Test
    void shouldReturnDifferentPrototypeInstances() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            Counter counter1 = context.getBean(Counter.class);
            Counter counter2 = context.getBean(Counter.class);

            counter1.increment();

            // Prototype beans should create a new instance every time.
            assertNotSame(counter1, counter2);

            // Each instance maintains its own state.
            assertEquals(1, counter1.getCount());
            assertEquals(0, counter2.getCount());
        }
    }
}
