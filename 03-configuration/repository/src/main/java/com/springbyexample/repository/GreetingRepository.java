package com.springbyexample.repository;

import org.springframework.stereotype.Repository;

/**
 * @author Mujuzi Moses
 */
@Repository
public class GreetingRepository {

    public String findGreeting() {
        return "Hello from GreetingRepository!";
    }

}
