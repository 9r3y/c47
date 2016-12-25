package com.y3r9.c47.dog.swj;

import com.y3r9.c47.dog.swj.model.parallel.spi.Partitioner;

/**
 * The class DataPartitioner.
 *
 * @version 1.0
 */
final class DataPartitioner implements Partitioner<Data> {

    @Override
    public int getPartSelector(final long token, final Data data) {
        return (int) token;
    }
}
