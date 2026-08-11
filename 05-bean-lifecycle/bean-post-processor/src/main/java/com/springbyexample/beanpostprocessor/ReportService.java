package com.springbyexample.beanpostprocessor;

import jakarta.annotation.PostConstruct;

/**
 * @author Mujuzi Moses
 */
public class ReportService {

    @PostConstruct
    public void initialize() {
        // Spring invokes this method during bean initialization.
        System.out.println("ReportService @PostConstruct");
    }

    public void generate() {
        System.out.println("Generating report...");
    }
}