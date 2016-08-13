package com.y3r9.c47.dog.swj.model.sort;

import com.y3r9.c47.dog.swj.model.sort.spi.HeapOut;

/**
 * The Class HeapDummyOut.
 * 
 * @param <V> the value type
 * @version 1.0
 * @see HeapOut
 * @since project 3.0
 */
public class HeapDummyOut<V> implements HeapOut<V> {

    @Override
    public final boolean hasNext() {
        return false;
    }

    @Override
    public final boolean isNextReady() {
        return false;
    }

    @Override
    public final long peekKey() {
        return 0;
    }

    @Override
    public final V peekValue() {
        return null;
    }

    @Override
    public final V pop() {
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("HeapDummyOut [isNextReady()=").append(isNextReady()).append(", peekKey()=")
                .append(peekKey()).append(", peekValue()=").append(peekValue()).append(']');
        return builder.toString();
    }

}
