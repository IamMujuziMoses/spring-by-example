package com.springbyexample.transactional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Mujuzi Moses
 */
@Repository
public class GreetingRepository {

    private final JdbcTemplate jdbcTemplate;

    public GreetingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String message) {
        jdbcTemplate.update("INSERT INTO greetings(message) VALUES (?)", message);
    }

    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM greetings", Integer.class);
    }
}
