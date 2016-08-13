package com.y3r9.c47.dog.swj.model.sort.spi;

/**
 * The Interface SortPipePut.
 * 
 * @param <V> the value type
 * @version 1.0
 * @see SortPipeThreshold
 * @since project 3.0
 */
public interface SortPipePut<V> extends SortPipeThreshold {

    /**
     * Put. Put the value into pipe directly.
     * 
     * Check the token with respect to the {@link #getInTokenThre()}, only if the token is smaller
     * than that returns by the {@link #getInTokenThre()} function, then the value will be put into
     * the pipe. Otherwise, it will be waiting till the condition happens.
     * 
     * @param token the token
     * @param value the value
     */
    void put(long token, V value);
}
