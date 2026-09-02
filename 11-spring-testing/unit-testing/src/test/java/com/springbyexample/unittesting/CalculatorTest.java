package com.springbyexample.unittesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Mujuzi Moses
 */
public class CalculatorTest {

    private final Calculator calculator = new Calculator();

    @Test
    void shouldAddTwoNumbers() {
        assertEquals(5, calculator.add(2, 3));
    }

    @Test
    void shouldSubtractTwoNumbers() {
        assertEquals(2, calculator.subtract(5, 3));
    }

    @Test
    void shouldMultiplyTwoNumbers() {
        assertEquals(15, calculator.multiply(5, 3));
    }

    @Test
    void shouldDivideTwoNumbers() {
        assertEquals(2, calculator.divide(6, 3));
    }
}