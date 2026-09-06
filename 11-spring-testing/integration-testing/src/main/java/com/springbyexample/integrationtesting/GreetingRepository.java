package com.springbyexample.integrationtesting;

import org.springframework.stereotype.Repository;

/**
 * @author Mujuzi Moses
 */
@Repository
public class GreetingRepository {

    public String findGreeting() {
        return "Hello from the repository!";
    }
}
