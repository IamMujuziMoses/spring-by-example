package com.springbyexample.transactionals;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * @author Mujuzi Moses
 */
public class TransactionalApplication {

    public static void main(String[] args) throws Throwable {
        GreetingService greetingService = new GreetingService();
        TransactionalService transactionalService = new TransactionalService(greetingService);
        RecordingTransactionManager transactionManager = new RecordingTransactionManager();
        TransactionAttributeSource attributeSource = new AnnotationTransactionAttributeSource();

        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(transactionManager);
        transactionInterceptor.setTransactionAttributeSource(attributeSource);

        Method greetMethod = TransactionalService.class.getMethod("greet");
        Object result = transactionInterceptor.invoke(new SimpleMethodInvocation(transactionalService, greetMethod));

        System.out.println(result);
        System.out.println("Transactions started: " + transactionManager.getTransactionCount());
        System.out.println("Transactions committed: " + transactionManager.getCommitCount());

        Method failMethod = TransactionalService.class.getMethod("fail");

        try {
            transactionInterceptor.invoke(new SimpleMethodInvocation(transactionalService, failMethod));
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Transactions rolled back: " + transactionManager.getRollbackCount());
    }

    private record SimpleMethodInvocation(Object target, Method method) implements MethodInvocation {

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