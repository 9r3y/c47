package com.y3r9.c47.dog.swj.model.sort.spi;

/**
 * The Interface HeapOut.
 * 
 * @param <V> the value type
 * @version 1.0
 * @since project 3.0
 */
public interface HeapOut<V> {

    /**
     * Checks if has next.
     * 
     * @return true, if has next
     */
    boolean hasNext();

    /**
     * Checks if is next ready.
     * 
     * @return true, if is ready
     */
    boolean isNextReady();

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
     * Pop the value.
     * 
     * @return the value , if no such element return null.
     */
    V pop();
}
