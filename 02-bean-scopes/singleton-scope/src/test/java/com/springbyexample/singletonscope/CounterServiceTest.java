package com.springbyexample.singletonscope;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class CounterServiceTest {

    @Test
    void shouldReturnSameSingletonInstance() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            // Retrieve the same bean twice from the ApplicationContext.
            Counter counter1 = context.getBean(Counter.class);
            Counter counter2 = context.getBean(Counter.class);

            // Update the state of the first reference.
            counter1.increment();

            // Both references should point to the same singleton instance.
            assertSame(counter1, counter2);

            // Since they reference the same object, the updated state is shared.
            assertEquals(1, counter2.getCount());
        }
    }
}
