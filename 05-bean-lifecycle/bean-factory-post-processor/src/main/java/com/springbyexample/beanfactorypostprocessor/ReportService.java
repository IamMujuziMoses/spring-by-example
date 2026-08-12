package com.springbyexample.beanfactorypostprocessor;

/**
 * @author Mujuzi Moses
 */
public class ReportService {

    private String reportName;

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public void generate() {
        System.out.println("Generating: " + reportName);
    }
}
