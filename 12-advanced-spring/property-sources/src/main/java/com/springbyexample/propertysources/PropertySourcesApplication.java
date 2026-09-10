package com.springbyexample.propertysources;

import java.util.Map;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

/**
 * @author Mujuzi Moses
 */
public class PropertySourcesApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext()) {

            ConfigurableEnvironment environment = applicationContext.getEnvironment();

            Map<String, Object> properties = Map.of("application.name", "Spring by Example",
                    "application.version", "1.0");

            MapPropertySource propertySource = new MapPropertySource("applicationProperties", properties);

            environment.getPropertySources().addFirst(propertySource);

            System.out.println("Application name: " + environment.getProperty("application.name"));
            System.out.println("Application version: " + environment.getProperty("application.version"));
        }
    }
}
