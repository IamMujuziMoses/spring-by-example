package com.springbyexample.circulardependencies;

import org.springframework.stereotype.Service;

/**
 * @author Mujuzi Moses
 */
@Service
public class ServiceA {

    private final ServiceB serviceB;

    public ServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
    }
}
