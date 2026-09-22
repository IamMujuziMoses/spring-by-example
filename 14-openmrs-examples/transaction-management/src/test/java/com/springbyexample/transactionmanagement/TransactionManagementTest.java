package com.springbyexample.transactionmanagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.springbyexample.transactionmanagement.impl.GreetingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.Advised;

/**
 * @author Mujuzi Moses
 */
public class TransactionManagementTest {

    private RecordingTransactionManager transactionManager;

    private GreetingService service;

    @BeforeEach
    void setUp() {
        transactionManager = new RecordingTransactionManager();

        TransactionalServiceContext serviceContext = new TransactionalServiceContext(transactionManager);
        serviceContext.setService(GreetingService.class, new GreetingServiceImpl());

        service = serviceContext.getService(GreetingService.class);
    }

    @Test
    void shouldCreateTransactionalProxy() {
        assertInstanceOf(Advised.class, service);
    }

    @Test
    void shouldStartAndCommitTransactionForSuccessfulInvocation() {
        assertEquals("Greeting saved successfully!", service.saveGreeting());

        assertEquals(1, transactionManager.getTransactionCount());
        assertEquals(1, transactionManager.getCommitCount());
        assertEquals(0, transactionManager.getRollbackCount());
    }

    @Test
    void shouldRollbackTransactionWhenServiceThrowsException() {
        assertThrows(IllegalStateException.class, () -> service.fail());

        assertEquals(1, transactionManager.getTransactionCount());
        assertEquals(0, transactionManager.getCommitCount());
        assertEquals(1, transactionManager.getRollbackCount());
    }

    @Test
    void shouldReturnSameTransactionalProxyForRepeatedLookups() {
        TransactionalServiceContext serviceContext = new TransactionalServiceContext(transactionManager);
        serviceContext.setService(GreetingService.class, new GreetingServiceImpl());

        GreetingService first = serviceContext.getService(GreetingService.class);
        GreetingService second = serviceContext.getService(GreetingService.class);

        assertEquals(first, second);
    }
}
