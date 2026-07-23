package com.springbyexample.setterinjection;

/**
 * @author Mujuzi Moses
 */

public class ReportService {

    private PrinterService printerService;

    public void setPrinterService(PrinterService printerService) {
        this.printerService = printerService;
    }

    public void generateReport() {
        if (printerService != null) {
            printerService.print("Monthly Sales Report");
        }
    }
}
