package com.springbyexample.smartlifecycle;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class AppConfigTest {

    @Test
    void shouldStartLifecycleBeanAutomatically() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var scheduler = context.getBean(ReportScheduler.class);

            assertTrue(scheduler.isRunning());
        }
    }

    @Test
    void shouldStopLifecycleBeanWhenContextCloses() {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);

        var scheduler = context.getBean(ReportScheduler.class);

        assertTrue(scheduler.isRunning());

        context.close();

        assertFalse(scheduler.isRunning());
    }
}
