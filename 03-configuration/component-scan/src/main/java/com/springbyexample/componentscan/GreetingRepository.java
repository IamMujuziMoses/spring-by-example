package com.springbyexample.componentscan;

import org.springframework.stereotype.Repository;

/**
 * @author Mujuzi Moses
 */
@Repository
public class GreetingRepository {

    public String getGreeting() {
        return "Hello from GreetingRepository!";
    }

}
