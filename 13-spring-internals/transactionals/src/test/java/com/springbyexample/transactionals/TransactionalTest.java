package com.springbyexample.transactionals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * @author Mujuzi Moses
 */
public class TransactionalTest {

    @Test
    void shouldCommitSuccessfulTransaction() throws Throwable {
        GreetingService greetingService = new GreetingService();
        TransactionalService service = new TransactionalService(greetingService);
        RecordingTransactionManager transactionManager = new RecordingTransactionManager();
        TransactionAttributeSource attributeSource = new AnnotationTransactionAttributeSource();

        TransactionInterceptor interceptor = new TransactionInterceptor();
        interceptor.setTransactionManager(transactionManager);
        interceptor.setTransactionAttributeSource(attributeSource);

        Method method = TransactionalService.class.getMethod("greet");
        Object result = interceptor.invoke(new TestMethodInvocation(service, method));

        assertEquals("Hello from GreetingService!", result);
        assertEquals(1, transactionManager.getTransactionCount());
        assertEquals(1, transactionManager.getCommitCount());
        assertEquals(0, transactionManager.getRollbackCount());
    }

    @Test
    void shouldRollbackFailedTransaction() throws Throwable {
        GreetingService greetingService = new GreetingService();
        TransactionalService service = new TransactionalService(greetingService);
        RecordingTransactionManager transactionManager = new RecordingTransactionManager();
        TransactionAttributeSource attributeSource = new AnnotationTransactionAttributeSource();

        TransactionInterceptor interceptor = new TransactionInterceptor();
        interceptor.setTransactionManager(transactionManager);
        interceptor.setTransactionAttributeSource(attributeSource);

        Method method = TransactionalService.class.getMethod("fail");

        assertThrows(IllegalStateException.class, () -> interceptor.invoke(new TestMethodInvocation(service, method)));
        assertEquals(1, transactionManager.getTransactionCount());
        assertEquals(0, transactionManager.getCommitCount());
        assertEquals(1, transactionManager.getRollbackCount());
    }

    private record TestMethodInvocation(Object target, Method method) implements MethodInvocation {

        @Override
        public Method getMethod() {
            return method;
        }

        @Override
        public Object[] getArguments() {
            return new Object[0];
        }

        @Override
        public Object proceed() throws Throwable {
            try {
                return method.invoke(target);
            } catch (InvocationTargetException exception) {
                throw exception.getCause();
            }
        }

        @Override
        public Object getThis() {
            return target;
        }

        @Override
        public AccessibleObject getStaticPart() {
            return method;
        }
    }
}
