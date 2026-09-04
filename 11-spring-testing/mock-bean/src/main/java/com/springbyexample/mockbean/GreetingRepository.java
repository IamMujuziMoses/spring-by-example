package com.springbyexample.mockbean;

import org.springframework.stereotype.Repository;

/**
 * @author Mujuzi Moses
 */
@Repository
public class GreetingRepository {

    public String findNameById(Long id) {
        return "Spring";
    }
}
