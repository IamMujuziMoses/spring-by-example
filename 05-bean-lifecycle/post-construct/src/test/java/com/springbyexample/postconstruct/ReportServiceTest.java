package com.springbyexample.postconstruct;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class ReportServiceTest {

    @Test
    void shouldInvokePostConstructDuringBeanInitialization() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            ReportService reportService = context.getBean(ReportService.class);

            assertTrue(reportService.isInitialized());
        }
    }

}
