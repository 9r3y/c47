package com.y3r9.c47.dog;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Created by zyq on 2014/8/13.
 */
@RunWith(value=Parameterized.class)
public class ParameterizedTest {

    private double expected;
    private double valueOne;
    private double valueTwo;

    @Parameterized.Parameters
    public static Collection dataParameters() {
        return Arrays.asList(new Object[][]{
                {2, 1, 1}, //expected, valueOne, valueTwo
                {3, 2, 1},
                {4, 3, 1},
        });
    }

    public ParameterizedTest(double expected,
                             double valueOne, double valueTwo) {
        this.expected = expected;
        this.valueOne = valueOne;
        this.valueTwo = valueTwo;
    }

    @Test
    public void sum() {
        Calculator calc = new Calculator();
        assertEquals(expected, calc.add(valueOne, valueTwo), 0);
        assertThat(6, anyOf(equalTo(4), equalTo(6)));
    }
}
