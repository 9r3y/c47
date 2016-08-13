package com.y3r9.c47.dog.swj.model.sort.spi;

/**
 * The Interface LongHeapOut.
 * 
 * @param <V> the value type
 * @version 1.0
 * @since project 3.0
 */
public interface LongHeapOut<V> {

    /**
     * Checks if is ready.
     * 
     * @return true, if is ready
     */
    boolean isReady();

    /**
     * Peek key.
     * 
     * @return the k
     */
    long peekKey();

    /**
     * Peek value.
     * 
     * @return the v
     */
    V peekValue();

    /**
     * Pop.
     */
    void pop();
}
