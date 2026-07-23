package com.springbyexample.setterinjection;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */

public class Main {
    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        ReportService service = context.getBean(ReportService.class);
        service.generateReport();
    }
}
