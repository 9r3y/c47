package com.y3r9.c47.dog.swj.model.parallel.spi;

/**
 * The Interface Partitioner.
 * 
 * @param <D> the generic data type
 * @version 1.0
 * @since project 3.0
 */
public interface Partitioner<D> {

    /**
     * Gets the partition selector.
     * 
     * @param token the token
     * @param data the data
     * @return the partition selector
     */
    int getPartSelector(long token, D data);
}
