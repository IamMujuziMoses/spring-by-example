package com.springbyexample.adviceordering;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

/**
 * @author Mujuzi Moses
 */
@Aspect
@Order(2)
public class SecurityAspect {

    @Before("execution(* com.springbyexample.adviceordering.GreetingService.greet(..))")
    public void checkAccess() {
        System.out.println("Security: Checking access");
    }

    @After("execution(* com.springbyexample.adviceordering.GreetingService.greet(..))")
    public void afterSecurityCheck() {
        System.out.println("Security: After greeting");
    }
}
