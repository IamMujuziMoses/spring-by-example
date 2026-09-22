package com.springbyexample.transactionmanagement;

import java.util.concurrent.atomic.AtomicInteger;

import org.jspecify.annotations.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;

/**
 * @author Mujuzi Moses
 */
public class RecordingTransactionManager implements PlatformTransactionManager {

    private final AtomicInteger transactionCount = new AtomicInteger();

    private final AtomicInteger commitCount = new AtomicInteger();

    private final AtomicInteger rollbackCount = new AtomicInteger();

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) {
        transactionCount.incrementAndGet();

        System.out.println("Transaction started");

        return new SimpleTransactionStatus();
    }

    @Override
    public void commit(@NonNull TransactionStatus status) {
        commitCount.incrementAndGet();

        System.out.println("Transaction committed");
    }

    @Override
    public void rollback(@NonNull TransactionStatus status) {
        rollbackCount.incrementAndGet();

        System.out.println("Transaction rolled back");
    }

    public int getTransactionCount() {
        return transactionCount.get();
    }

    public int getCommitCount() {
        return commitCount.get();
    }

    public int getRollbackCount() {
        return rollbackCount.get();
    }
}
