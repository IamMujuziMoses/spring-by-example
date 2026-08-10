package com.springbyexample.disposablebean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class ReportServiceTest {

    @Test
    void shouldInvokeDestroyWhenContextIsClosed() {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);

        ReportService reportService = context.getBean(ReportService.class);

        assertTrue(reportService.isActive());

        context.close();

        assertFalse(reportService.isActive());
    }
}
