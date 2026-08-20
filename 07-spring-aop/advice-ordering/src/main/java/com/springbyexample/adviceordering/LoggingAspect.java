package com.springbyexample.adviceordering;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

/**
 * @author Mujuzi Moses
 */
@Aspect
@Order(1)
public class LoggingAspect {

    @Before("execution(* com.springbyexample.adviceordering.GreetingService.greet(..))")
    public void beforeGreeting() {
        System.out.println("Logging: Before greeting");
    }

    @After("execution(* com.springbyexample.adviceordering.GreetingService.greet(..))")
    public void afterGreeting() {
        System.out.println("Logging: After greeting");
    }
}