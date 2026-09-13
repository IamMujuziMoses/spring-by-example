package com.springbyexample.conversionservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * @author Mujuzi Moses
 */
public class ConversionServiceApplicationTest {

    @Test
    void shouldConvertStringToInteger() {
        ConversionService conversionService = new DefaultConversionService();

        Integer result = conversionService.convert("42", Integer.class);

        assertEquals(42, result);
    }

    @Test
    void shouldConvertStringToBoolean() {
        ConversionService conversionService = new DefaultConversionService();

        Boolean result = conversionService.convert("true", Boolean.class);

        assertTrue(result);
    }

    @Test
    void shouldCheckConversionSupport() {
        ConversionService conversionService = new DefaultConversionService();

        assertTrue(conversionService.canConvert(String.class, Integer.class));

        assertTrue(conversionService.canConvert(String.class, Boolean.class));
    }
}
