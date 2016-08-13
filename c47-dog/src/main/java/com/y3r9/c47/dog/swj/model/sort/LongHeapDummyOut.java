package com.y3r9.c47.dog.swj.model.sort;

import com.y3r9.c47.dog.swj.model.sort.spi.LongHeapOut;

/**
 * The Class LongHeapDummyOut.
 * 
 * @param <V> the value type
 * @version 1.0
 * @see LongHeapOut
 * @since project 3.0
 */
public class LongHeapDummyOut<V> implements LongHeapOut<V> {

    @Override
    public final boolean isReady() {
        return false;
    }

    @Override
    public final long peekKey() {
        return -1;
    }

    @Override
    public final V peekValue() {
        return null;
    }

    @Override
    public final void pop() {
        // do nothing
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("LongHeapDummyOut [isReady()=").append(isReady())
                .append(", peekKey()=").append(peekKey()).append(", peekValue()=")
                .append(peekValue()).append(']');
        return builder.toString();
    }
}
