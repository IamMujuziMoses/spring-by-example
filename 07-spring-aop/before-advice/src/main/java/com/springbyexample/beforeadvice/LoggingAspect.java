package com.springbyexample.beforeadvice;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author Mujuzi Moses
 */
@Aspect
public class LoggingAspect {

    @Before("execution(* com.springbyexample.beforeadvice.GreetingService.greet(..))")
    public void beforeAdvice() {
        System.out.println("Before greeting");
    }
}
