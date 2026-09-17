package com.springbyexample.aopproxies;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Mujuzi Moses
 */
public class LoggingInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        System.out.println("Before method invocation");

        Object result = invocation.proceed();

        System.out.println("After method invocation");

        return result;
    }
}
