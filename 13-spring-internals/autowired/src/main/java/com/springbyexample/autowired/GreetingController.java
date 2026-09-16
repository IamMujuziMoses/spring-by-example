package com.springbyexample.autowired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class GreetingController {

    @Autowired
    private GreetingService greetingService;

    public String greet() {
        return greetingService.greet();
    }

    public GreetingService getGreetingService() {
        return greetingService;
    }
}
