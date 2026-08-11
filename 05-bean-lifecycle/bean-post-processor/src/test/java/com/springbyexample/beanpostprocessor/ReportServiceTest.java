package com.springbyexample.beanpostprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class ReportServiceTest {

    @Test
    void shouldProcessReportServiceBeforeAndAfterInitialization() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var processor = context.getBean(LoggingBeanPostProcessor.class);

            var reportService = context.getBean(ReportService.class);

            assertNotNull(reportService);
            assertEquals(List.of("before:reportService", "after:reportService"), processor.getEvents());
        }
    }

}
