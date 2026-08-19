package com.springbyexample.pointcuts;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author Mujuzi Moses
 */
@Aspect
public class LoggingAspect {

    @Before("execution(* com.springbyexample.pointcuts.GreetingService.greet(..))")
    public void logGreeting() {
        System.out.println("Greeting method called");
    }
}
