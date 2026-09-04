package com.springbyexample.springboottest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mujuzi Moses
 */
@SpringBootTest
public class GreetingServiceTest {

    @Autowired
    private GreetingService greetingService;

    @Test
    void shouldCreateGreeting() {
        String result = greetingService.greet("Spring");

        assertEquals("Hello, Spring!", result);
    }
}