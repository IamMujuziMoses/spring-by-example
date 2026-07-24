package com.springbyexample.setterinjection;

/**
 * @author Mujuzi Moses
 */

public class ReportService {

    private PrinterService printerService;

    public void setPrinterService(PrinterService printerService) {
        this.printerService = printerService;
    }

    public void generateReport(String report) {
        if (printerService != null) {
            printerService.print(report);
        }
    }
}
