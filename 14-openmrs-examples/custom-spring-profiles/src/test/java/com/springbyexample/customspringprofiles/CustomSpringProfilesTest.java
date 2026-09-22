package com.springbyexample.customspringprofiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class CustomSpringProfilesTest {

    @Test
    void shouldLoadDevelopmentImplementationWhenDevelopmentProfileIsActive() {
        AnnotationConfigApplicationContext context = createContext("development");

        GreetingService service = context.getBean(GreetingService.class);

        assertInstanceOf(DevelopmentGreetingService.class, service);
        assertEquals("Hello from the development environment!", service.greet());

        context.close();
    }

    @Test
    void shouldLoadProductionImplementationWhenProductionProfileIsActive() {
        AnnotationConfigApplicationContext context = createContext("production");

        GreetingService service = context.getBean(GreetingService.class);

        assertInstanceOf(ProductionGreetingService.class, service);
        assertEquals("Hello from the production environment!", service.greet());

        context.close();
    }

    @Test
    void shouldExposeActiveDevelopmentProfile() {
        AnnotationConfigApplicationContext context = createContext("development");

        assertEquals("development", context.getEnvironment().getActiveProfiles()[0]);

        context.close();
    }

    @Test
    void shouldExposeActiveProductionProfile() {
        AnnotationConfigApplicationContext context = createContext("production");

        assertEquals("production", context.getEnvironment().getActiveProfiles()[0]);

        context.close();
    }

    private AnnotationConfigApplicationContext createContext(String profile) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles(profile);
        context.register(OpenmrsProfileConfig.class);
        context.refresh();

        return context;
    }
}
