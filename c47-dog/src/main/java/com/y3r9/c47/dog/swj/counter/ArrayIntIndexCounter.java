package com.y3r9.c47.dog.swj.counter;

import java.util.Arrays;

import cn.com.netis.dp.commons.lang.Constants;
import cn.com.netis.dp.commons.lang.NonPositiveArgumentException;

/**
 * The Class ArrayIntIndexCounter.
 * 
 * @version 1.2
 * @see IntIndexCounter
 * @since project 2.0
 */
public final class ArrayIntIndexCounter implements IntIndexCounter {

    /**
     * New instance.
     * 
     * @param size the size
     * @return the index counter
     */
    public static IntIndexCounter newInstance(final int size) {
        NonPositiveArgumentException.check(size, "array size of ArrayIntIndexCounter");
        return new ArrayIntIndexCounter(size);
    }

    @Override
    public int get(final int counterIndex) {
        return counters[counterIndex];
    }

    @Override
    public void count(final int counterIndex, final int value) {
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
        builder.append("ArrayIntIndexCounter [counters=").append(Arrays.toString(counters))
                .append(']');
        return builder.toString();
    }

    /**
     * Instantiates a new array index counter.
     * 
     * @param size the size
     */
    private ArrayIntIndexCounter(final int size) {
        counters = new int[size];
    }

    /** The counters. */
    private final transient int[] counters;
}
