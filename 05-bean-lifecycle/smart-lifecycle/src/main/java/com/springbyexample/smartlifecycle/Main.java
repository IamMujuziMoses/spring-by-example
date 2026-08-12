package com.springbyexample.smartlifecycle;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class Main {

    public static void main(String[] args) {

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            var scheduler = context.getBean(ReportScheduler.class);

            System.out.println("Scheduler running: " + scheduler.isRunning());
        }
    }
}
