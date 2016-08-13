package com.y3r9.c47.dog.swj.counter;

import java.util.Arrays;

import cn.com.netis.dp.commons.lang.Constants;
import cn.com.netis.dp.commons.lang.NonPositiveArgumentException;

/**
 * The Class ArrayLongIndexCounter.
 * 
 * @version 1.2
 * @see LongIndexCounter
 * @since project 2.0
 */
public final class ArrayLongIndexCounter implements LongIndexCounter {

    /**
     * New instance.
     * 
     * @param size the size
     * @return the index counter
     */
    public static LongIndexCounter newInstance(final int size) {
        NonPositiveArgumentException.check(size, "array size of ArrayLongIndexCounter");
        return new ArrayLongIndexCounter(size);
    }

    @Override
    public long get(final int counterIndex) {
        return counters[counterIndex];
    }

    @Override
    public void count(final int counterIndex, final long value) {
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
        builder.append("ArrayLongIndexCounter [counters=").append(Arrays.toString(counters))
                .append(']');
        return builder.toString();
    }

    /**
     * Instantiates a new array index counter.
     * 
     * @param size the size
     */
    private ArrayLongIndexCounter(final int size) {
        counters = new long[size];
    }

    /** The counters. */
    private final transient long[] counters;
}
