package com.springbyexample.environment;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @author Mujuzi Moses
 */
public class EnvironmentApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext()) {

            Environment environment = applicationContext.getEnvironment();

            System.out.println("Active profiles: " + String.join(", ", environment.getActiveProfiles()));
            System.out.println("Default profiles: " + String.join(", ", environment.getDefaultProfiles()));
        }
    }
}
