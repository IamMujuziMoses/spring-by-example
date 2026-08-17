package com.springbyexample.cglibproxy;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author Mujuzi Moses
 */
public class LoggingMethodInterceptor implements MethodInterceptor {

    private boolean invoked;

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        invoked = true;

        System.out.println("Before method: " + method.getName());

        Object result = methodProxy.invokeSuper(object, args);

        System.out.println("After method: " + method.getName());

        return result;

    }

    public boolean wasInvoked() {

        return invoked;

    }
}
