package com.springbyexample.isolationlevels;

import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Mujuzi Moses
 */
@Repository
public class AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal findBalance(Long accountId) {
        return jdbcTemplate.queryForObject("SELECT balance FROM accounts WHERE id = ?", BigDecimal.class, accountId);
    }

    public void updateBalance(Long accountId, BigDecimal balance) {
        jdbcTemplate.update("UPDATE accounts SET balance = ? WHERE id = ?", balance, accountId);
    }
}
