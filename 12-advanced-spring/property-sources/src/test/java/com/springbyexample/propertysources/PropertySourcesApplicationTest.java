package com.springbyexample.propertysources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

/**
 * @author Mujuzi Moses
 */
public class PropertySourcesApplicationTest {

    @Test
    void shouldAddAndRetrievePropertySource() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext()) {

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource propertySource = new MapPropertySource("applicationProperties",
                            Map.of("application.name", "Spring by Example"));

            environment.getPropertySources().addFirst(propertySource);

            assertNotNull(environment.getPropertySources().get("applicationProperties"));
            assertEquals("Spring by Example", environment.getProperty("application.name"));
        }
    }

    @Test
    void shouldResolvePropertiesFromMultiplePropertySources() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext()) {

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            environment.getPropertySources().addFirst(new MapPropertySource("applicationProperties",
                            Map.of("application.name", "Spring by Example", "application.version", "1.0")));

            assertEquals("Spring by Example", environment.getProperty("application.name"));
            assertEquals("1.0", environment.getProperty("application.version"));
        }
    }

    @Test
    void shouldUseFirstPropertySourceWhenPropertyExistsInMultipleSources() {
        try (AnnotationConfigApplicationContext applicationContext =
                     new AnnotationConfigApplicationContext()) {

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            environment.getPropertySources().addLast(new MapPropertySource("lowerPriority",
                    Map.of("application.name", "Lower Priority")));
            environment.getPropertySources().addFirst(new MapPropertySource("higherPriority",
                            Map.of("application.name", "Higher Priority")));

            assertEquals("Higher Priority", environment.getProperty("application.name"));
        }
    }
}