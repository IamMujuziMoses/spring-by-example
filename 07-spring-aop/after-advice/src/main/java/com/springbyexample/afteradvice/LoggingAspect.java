package com.springbyexample.afteradvice;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Mujuzi Moses
 */
@Aspect
public class LoggingAspect {

    @After("execution(* com.springbyexample.afteradvice.GreetingService.greet(..))")
    public void afterAdvice() {
        System.out.println("After greeting");
    }
}
