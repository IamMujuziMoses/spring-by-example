package com.springbyexample.configurationproperties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Mujuzi Moses
 */
@SpringBootTest
public class ConfigurationPropertiesTest {

    @Autowired
    private AppProperties properties;

    @Test
    void shouldBindConfigurationProperties() {
        assertEquals("Spring by Example", properties.getName());
        assertEquals("Learning Spring Boot through small runnable examples", properties.getDescription());
        assertEquals("1.0", properties.getVersion());

    }
}
