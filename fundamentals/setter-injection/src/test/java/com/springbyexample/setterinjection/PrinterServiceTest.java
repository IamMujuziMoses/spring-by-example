package com.springbyexample.setterinjection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Mujuzi Moses
 */
public class PrinterServiceTest {

    @Test
    void shouldCreatePrinterServiceBean() {

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        PrinterService printerService = context.getBean(PrinterService.class);
        assertNotNull(printerService);
    }
}
