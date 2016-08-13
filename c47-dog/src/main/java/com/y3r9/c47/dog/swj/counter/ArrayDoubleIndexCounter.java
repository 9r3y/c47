package com.y3r9.c47.dog.swj.counter;

import java.util.Arrays;

import cn.com.netis.dp.commons.lang.Constants;
import cn.com.netis.dp.commons.lang.NonPositiveArgumentException;

/**
 * The Class ArrayDoubleIndexCounter.
 * 
 * @version 1.1
 * @see DoubleIndexCounter
 * @since project 2.0
 */
public final class ArrayDoubleIndexCounter implements DoubleIndexCounter {

    /**
     * New instance.
     * 
     * @param size the size
     * @return the double index counter
     */
    public static DoubleIndexCounter newInstance(final int size) {
        NonPositiveArgumentException.check(size, "array size of ArrayDoubleIndexCounter");
        return new ArrayDoubleIndexCounter(size);
    }

    @Override
    public double get(final int counterIndex) {
        return counters[counterIndex];
    }

    @Override
    public void count(final int counterIndex, final double value) {
        counters[counterIndex] += value;
    }

    @Override
    public int getCounterSize() {
        return counters.length;
    }

    @Override
    public void clear() {
        for (int i = 0; i < counters.length; i++) {
            counters[i] = 0;
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(Constants.STRING_BUILDER_MIDDLE_SIZE);
        builder.append("ArrayDoubleIndexCounter [counters=").append(Arrays.toString(counters))
                .append(']');
        return builder.toString();
    }

    /**
     * Instantiates a new array double index counter.
     * 
     * @param size the size
     */
    private ArrayDoubleIndexCounter(final int size) {
        counters = new double[size];
    }

    /** The counters. */
    private final transient double[] counters;
}
