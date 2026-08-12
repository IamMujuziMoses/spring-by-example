package com.springbyexample.beanfactorypostprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class ReportServiceTest {

    @Test
    void shouldModifyBeanDefinitionBeforeBeanCreation() {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var reportService = context.getBean(ReportService.class);

            assertEquals("Monthly Sales Report", reportService.getReportName());
        }
    }
}
