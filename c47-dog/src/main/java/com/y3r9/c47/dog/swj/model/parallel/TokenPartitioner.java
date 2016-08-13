package com.y3r9.c47.dog.swj.model.parallel;

import com.y3r9.c47.dog.swj.model.parallel.spi.Partitioner;

/**
 * The Class TokenPartitioner.
 * 
 * @param <D> the generic type
 * @version 1.0
 * @see Partitioner
 * @since project 3.0
 */
public final class TokenPartitioner<D> implements Partitioner<D> {

    @Override
    public int getPartSelector(final long token, final D data) {
        return (int) token;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TokenPartitioner [(int) token]");
        return builder.toString();
    }
}
