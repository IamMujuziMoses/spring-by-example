package com.springbyexample.initializingbean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var reportService = context.getBean(ReportService.class);

            System.out.println("Initialized: " + reportService.isInitialized());
        }
    }
}
