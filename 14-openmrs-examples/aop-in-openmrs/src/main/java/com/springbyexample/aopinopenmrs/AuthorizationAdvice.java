package com.springbyexample.aopinopenmrs;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jspecify.annotations.NonNull;

/**
 * @author Mujuzi Moses
 */
public class AuthorizationAdvice implements MethodInterceptor {

    private boolean authorized;

    public AuthorizationAdvice(boolean authorized) {
        this.authorized = authorized;
    }

    @Override
    public Object invoke(@NonNull MethodInvocation invocation) throws Throwable {

        if (!authorized) {
            throw new SecurityException("User is not authorized to invoke " + invocation.getMethod().getName());
        }

        return invocation.proceed();
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }
}
