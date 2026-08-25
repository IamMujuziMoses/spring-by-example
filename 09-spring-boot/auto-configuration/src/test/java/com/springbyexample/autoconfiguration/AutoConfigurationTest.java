package com.springbyexample.autoconfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Mujuzi Moses
 */
public class AutoConfigurationTest {

    @Test
    void shouldAutoConfigureJdbcTemplate() {
        try (ConfigurableApplicationContext context = SpringApplication.run(Application.class)) {

            JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);

            assertNotNull(jdbcTemplate);
        }
    }

    @Test
    void shouldAutoConfigureGreetingRepository() {
        try (ConfigurableApplicationContext context = SpringApplication.run(Application.class)) {

            GreetingRepository repository = context.getBean(GreetingRepository.class);

            assertNotNull(repository);
        }
    }

    @Test
    void shouldUseAutoConfiguredJdbcTemplate() {
        try (ConfigurableApplicationContext context = SpringApplication.run(Application.class)) {

            GreetingRepository repository = context.getBean(GreetingRepository.class);

            repository.save("Hello, Spring!");

            assertEquals(1, repository.count());
        }
    }
}
