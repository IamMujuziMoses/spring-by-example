package com.springbyexample.creatinganaspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Aspect
@Component
public class OrderLoggingAspect {

    private boolean invoked;

    @Before("execution(* com.springbyexample.creatinganaspect.OrderService.createOrder(..))")
    public void logBeforeCreateOrder() {
        invoked = true;
        System.out.println("About to create order");
    }

    public boolean wasInvoked() {
        return invoked;
    }
}