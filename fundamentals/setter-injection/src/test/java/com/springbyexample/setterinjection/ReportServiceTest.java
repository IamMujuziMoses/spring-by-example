package com.springbyexample.setterinjection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class ReportServiceTest {

    @Test
    void shouldCreateReportServiceBean() {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        ReportService reportService = context.getBean(ReportService.class);
        assertNotNull(reportService);
    }

    @Test
    void shouldInjectPrinterServiceUsingSetter() {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        ReportService reportService = context.getBean(ReportService.class);
        assertNotNull(reportService);

        // If Spring failed to inject PrinterService,
        // calling generateReport() would fail.
        reportService.generateReport();
    }
}
