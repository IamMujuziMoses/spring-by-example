package com.springbyexample.objectprovider;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
public class ReportManager {

    private final ObjectProvider<Report> reportProvider;

    public ReportManager(ObjectProvider<Report> reportProvider) {

        this.reportProvider = reportProvider;
    }

    public void generateReports() {

        Report firstReport = reportProvider.getObject();
        firstReport.generate();

        Report secondReport = reportProvider.getObject();
        secondReport.generate();

        System.out.println("firstReport == secondReport ::: " + firstReport.equals(secondReport));
    }
}
