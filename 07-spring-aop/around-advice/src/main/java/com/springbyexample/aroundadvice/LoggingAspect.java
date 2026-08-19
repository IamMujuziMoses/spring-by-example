package com.springbyexample.aroundadvice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Mujuzi Moses
 */
@Aspect
public class LoggingAspect {

    @Around("execution(* com.springbyexample.aroundadvice.GreetingService.greet(..))")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("Before greeting");

        Object result = joinPoint.proceed();

        System.out.println("After greeting");

        return result;
    }
}
