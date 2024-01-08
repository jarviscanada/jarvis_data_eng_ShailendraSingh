package ca.jrvs.apps.SimpleCalculator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleCalculatorTest
{
    SimpleCalculator calculator;

    @Before
    public void setUp() throws Exception
    {
        calculator = new SimpleCalculatorImpl();
    }

    @Test
    public void add()
    {
        int expected = 8;
        int actual = calculator.add(5,3);
        assertEquals(expected, actual);
    }

    @Test
    public void subtract()
    {
        int expected = 2;
        int actual = calculator.subtract(5,3);
        assertEquals(expected, actual);
    }

    @Test
    public void multiply()
    {
        int expected = 15;
        int actual = calculator.multiply(5,3);
        assertEquals(expected, actual);
    }

    @Test
    public void divide()
    {
        double expected = 5;
        double actual = calculator.divide(15,3);
        assertEquals(expected, actual, 0); //Asserts equality between doubles and allows 0 error
    }

    @Test
    public void power()
    {
        int expected = 8;
        int actual = calculator.power(2,3);
        assertEquals(expected, actual);
    }

    @Test
    public void abs()
    {
        double expected = 8;
        double actual = calculator.abs(-8);
        assertEquals(expected, actual, 0);
    }
}