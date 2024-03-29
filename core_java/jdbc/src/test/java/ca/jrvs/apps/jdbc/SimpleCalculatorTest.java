package ca.jrvs.apps.jdbc;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleCalculatorTest {

    static SimpleCalculator calculator;

    @BeforeClass
    public static void init() {
        calculator = new SimpleCalculatorImp();
    }

    @Test
    public void test_add() {
        int expected = 2;
        int actual = calculator.add(1, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void test_subtract() {
        int expected = 7;
        int actual = calculator.subtract(15, 8);
        assertEquals(expected, actual);
    }

    @Test
    public void test_multiply() {
        int expected = 16;
        int actual = calculator.multiply(4, 4);
        assertEquals(expected, actual);
    }

    @Test
    public void test_divide() {
        double expected = 5;
        double actual = calculator.divide(50, 10);
        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void test_power() {
        double expected = 16;
        double actual = calculator.power(4, 2);
        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void test_abs() {
        double expected = 6;
        double actual = calculator.abs(-6);
        assertEquals(expected, actual, 0.01);
    }
}