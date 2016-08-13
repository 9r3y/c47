package com.y3r9.c47.dog.swj.model.sort.spi;

/**
 * The Interface LongHeapIn.
 * 
 * @param <V> the value type
 * @version 1.0
 * @since project 3.0
 */
public interface LongHeapIn<V> {

    /**
     * Sets the left.
     * 
     * @param queue the queue
     * @return true, if successful
     */
    boolean setLeft(LongHeapOut<V> queue);

    /**
     * Gets the left.
     * 
     * @return the left
     */
    LongHeapOut<V> getLeft();

    /**
     * Sets the right.
     * 
     * @param queue the queue
     * @return true, if successful
     */
    boolean setRight(LongHeapOut<V> queue);

    /**
     * Gets the right.
     * 
     * @return the right
     */
    LongHeapOut<V> getRight();
}
