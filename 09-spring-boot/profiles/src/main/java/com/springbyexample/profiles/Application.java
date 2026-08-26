package com.springbyexample.profiles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Mujuzi Moses
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Value("${app.environment}")
    private String environment;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Active environment: " + environment);
    }
}
