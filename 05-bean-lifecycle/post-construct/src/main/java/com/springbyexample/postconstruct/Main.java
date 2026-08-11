package com.springbyexample.postconstruct;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            ReportService reportService =context.getBean(ReportService.class);

            System.out.println("Initialized: " + reportService.isInitialized());
        }
    }

}
