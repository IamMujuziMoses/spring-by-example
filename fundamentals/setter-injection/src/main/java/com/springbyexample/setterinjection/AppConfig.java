package com.springbyexample.setterinjection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mujuzi Moses
 */

@Configuration
public class AppConfig {

    @Bean
    public PrinterService printerService() {
        return new PrinterService();
    }

    @Bean
    public ReportService reportService(PrinterService printerService) {
        ReportService reportService = new ReportService();
        reportService.setPrinterService(printerService);

        return reportService;
    }
}
