package com.springbyexample.circulardependencies;

import org.springframework.stereotype.Service;

/**
 * @author Mujuzi Moses
 */
@Service
public class ServiceB {

    private final ServiceA serviceA;

    public ServiceB(ServiceA serviceA) {
        this.serviceA = serviceA;
    }
}
