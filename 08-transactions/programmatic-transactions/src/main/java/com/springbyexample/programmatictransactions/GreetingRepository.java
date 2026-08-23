package com.springbyexample.programmatictransactions;

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
        jdbcTemplate.update("INSERT INTO greetings (message) VALUES (?)", message);
    }

    public int count() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM greetings", Integer.class);

        return count != null ? count : 0;
    }
}
