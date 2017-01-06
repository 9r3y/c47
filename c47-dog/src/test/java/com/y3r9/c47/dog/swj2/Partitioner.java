package com.y3r9.c47.dog.swj2;

/**
 * The class Partitioner.
 *
 * @version 1.0
 */
final class Partitioner implements Partitionable<Data> {
    @Override
    public int partition(final Data data) {
        return token++ & partCountMask;
    }

    @Override
    public void setPartitionCount(final int count) {
        partCountMask = count - 1;
    }

    private int token;

    private int partCountMask;
}
