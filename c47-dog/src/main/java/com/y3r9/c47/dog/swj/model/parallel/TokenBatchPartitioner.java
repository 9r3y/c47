package com.y3r9.c47.dog.swj.model.parallel;

import com.y3r9.c47.dog.swj.model.parallel.spi.Partitioner;

/**
 * The Class TokenBatchPartitioner.
 * 
 * @param <D> the generic type
 * @version 1.0
 * @see Partitioner
 * @since project 3.0
 */
public final class TokenBatchPartitioner<D> implements Partitioner<D> {

    /** The Constant RSHIFT. */
    public static final int RSHIFT = 8;

    @Override
    public int getPartSelector(final long token, final D data) {
        return (int) (token >> RSHIFT);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TokenBatchPartitioner [(int) (token >> 8)]");
        return builder.toString();
    }
}
