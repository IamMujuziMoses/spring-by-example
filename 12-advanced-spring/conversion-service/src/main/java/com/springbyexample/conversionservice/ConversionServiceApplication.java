package com.springbyexample.conversionservice;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * @author Mujuzi Moses
 */
public class ConversionServiceApplication {

    public static void main(String[] args) {
        ConversionService conversionService = new DefaultConversionService();

        Integer number = conversionService.convert("42", Integer.class);
        Boolean enabled = conversionService.convert("true", Boolean.class);

        System.out.println("Number: " + number);
        System.out.println("Enabled: " + enabled);
    }
}