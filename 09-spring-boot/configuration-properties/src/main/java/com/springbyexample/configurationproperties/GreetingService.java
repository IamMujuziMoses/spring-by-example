package com.springbyexample.configurationproperties;

import org.springframework.stereotype.Service;

/**
 * @author Mujuzi Moses
 */
@Service
public class GreetingService {

    private final AppProperties properties;

    public GreetingService(AppProperties properties) {
        this.properties = properties;
    }

    public void printApplicationInfo() {
        System.out.println("Name: " + properties.getName());
        System.out.println("Description: " + properties.getDescription());
        System.out.println("Version: " + properties.getVersion());
    }
}
