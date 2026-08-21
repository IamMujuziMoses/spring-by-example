package com.springbyexample.isolationlevels;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mujuzi Moses
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BigDecimal readWithReadCommitted(Long accountId) {
        return accountRepository.findBalance(accountId);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public BigDecimal readWithReadUncommitted(Long accountId) {
        return accountRepository.findBalance(accountId);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BigDecimal readWithRepeatableRead(Long accountId) {
        return accountRepository.findBalance(accountId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BigDecimal readWithSerializable(Long accountId) {
        return accountRepository.findBalance(accountId);
    }

    @Transactional
    public void updateBalance(Long accountId, BigDecimal balance) {
        accountRepository.updateBalance(accountId, balance);
    }
}