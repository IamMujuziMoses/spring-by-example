package com.springbyexample.objectprovider;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            ReportManager manager = context.getBean(ReportManager.class);

            manager.generateReports();
        }
    }
}
