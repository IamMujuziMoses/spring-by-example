package com.springbyexample.controller;

import org.springframework.stereotype.Controller;

/**
 * @author Mujuzi Moses
 */
@Controller
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public String greet() {
        return greetingService.getGreeting();
    }

}
