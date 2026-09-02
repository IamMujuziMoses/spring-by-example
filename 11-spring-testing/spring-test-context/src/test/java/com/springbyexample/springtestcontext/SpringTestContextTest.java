package com.springbyexample.springtestcontext;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Mujuzi Moses
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestContextApplication.class)
public class SpringTestContextTest {

    @Autowired
    private GreetingService greetingService;

    @Test
    void shouldCreateGreeting() {
        String result = greetingService.greet("Spring");

        assertEquals("Hello, Spring!", result);
    }
}
