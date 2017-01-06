package com.y3r9.c47.dog.swj2;

/**
 * The interface Partitioner.
 *
 * @version 1.0
 */
interface Partitionable<T> {

    int partition(T data);

    void setPartitionCount(int count);
}
