package com.springbyexample.aopinopenmrs;

import java.util.concurrent.atomic.AtomicInteger;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Mujuzi Moses
 */
public class LoggingAdvice implements MethodInterceptor {

    private final AtomicInteger invocationCount = new AtomicInteger();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        invocationCount.incrementAndGet();

        System.out.println("Calling service method: " + invocation.getMethod().getName());

        Object result = invocation.proceed();

        System.out.println("Completed service method: " + invocation.getMethod().getName());

        return result;
    }

    public int getInvocationCount() {
        return invocationCount.get();
    }
}
