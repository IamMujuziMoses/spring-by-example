package com.springbyexample.afterreturningadvice;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Mujuzi Moses
 */
@Aspect
public class LoggingAspect {

    @AfterReturning(pointcut = "execution(* com.springbyexample.afterreturningadvice.GreetingService.greet(..))",
            returning = "result")
    public void afterReturningAdvice(String result) {
        System.out.println("Returned: " + result);
    }
}
