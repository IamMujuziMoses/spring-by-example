package com.springbyexample.configurationproperties;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * @author Mujuzi Moses
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class Application implements CommandLineRunner {

    private final AppProperties properties;

    public Application(AppProperties properties) {
        this.properties = properties;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Application: " + properties.getName());
        System.out.println("Description: " + properties.getDescription());
        System.out.println("Version: " + properties.getVersion());
    }
}
