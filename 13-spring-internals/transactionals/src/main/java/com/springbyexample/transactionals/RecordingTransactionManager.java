package com.springbyexample.transactionals;

import org.jspecify.annotations.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;

/**
 * @author Mujuzi Moses
 */
public class RecordingTransactionManager implements PlatformTransactionManager {

    private int transactionCount;

    private int commitCount;

    private int rollbackCount;

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) {
        transactionCount++;
        return new SimpleTransactionStatus();
    }

    @Override
    public void commit(@NonNull TransactionStatus status) {
        commitCount++;
    }

    @Override
    public void rollback(@NonNull TransactionStatus status) {
        rollbackCount++;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public int getCommitCount() {
        return commitCount;
    }

    public int getRollbackCount() {
        return rollbackCount;
    }
}
