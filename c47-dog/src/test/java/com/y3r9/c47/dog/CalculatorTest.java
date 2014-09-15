package com.y3r9.c47.dog;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by zyq on 2014/8/13.
 */

public class CalculatorTest {

    @Test
    public void testAdd() {

    	Calculator calculator = new Calculator();
        double result = calculator.add(10, 10);
        assertEquals(20, result, 0);

    }

}
