package com.springbyexample.testconfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mujuzi Moses
 */
@SpringBootTest
@Import(TestGreetingConfiguration.class)
public class GreetingServiceTest {

    @Autowired
    private GreetingService greetingService;

    @Test
    void shouldUseTestConfiguration() {
        String result = greetingService.greet();

        assertEquals("Hello from the test!", result);
    }
}
