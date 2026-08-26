package com.springbyexample.profiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Mujuzi Moses
 */
@SpringBootTest
@ActiveProfiles("dev")
public class ProfilesTest {

    @Value("${app.environment}")
    private String environment;

    @Test
    void shouldLoadDevelopmentProfile() {
        assertEquals("development", environment);
    }
}
