package com.springbyexample.aopinopenmrs;

import com.springbyexample.aopinopenmrs.impl.GreetingServiceImpl;

/**
 * @author Mujuzi Moses
 */
public class AopInOpenmrsApplication {

    public static void main(String[] args) {
        GreetingService greetingService = new GreetingServiceImpl();
        LoggingAdvice loggingAdvice = new LoggingAdvice();

        AopServiceContext serviceContext = new AopServiceContext();
        serviceContext.setService(GreetingService.class, greetingService, loggingAdvice);

        GreetingService proxiedService = serviceContext.getService(GreetingService.class);

        System.out.println(proxiedService.greet());
    }
}
