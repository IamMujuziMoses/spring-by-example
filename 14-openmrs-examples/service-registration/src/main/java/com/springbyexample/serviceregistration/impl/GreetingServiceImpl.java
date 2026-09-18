package com.springbyexample.serviceregistration.impl;

import com.springbyexample.serviceregistration.GreetingService;
import org.springframework.stereotype.Service;

/**
 * @author Mujuzi Moses
 */
@Service("greetingService")
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greet() {
        return "Hello from an OpenMRS service!";
    }
}
