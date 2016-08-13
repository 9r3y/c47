package com.y3r9.c47.dog.swj.model.sort.spi;

/**
 * The Interface HeapIn.
 * 
 * @param <V> the value type
 * @version 1.0
 * @since project 3.0
 */
public interface HeapIn<V> {

    /**
     * Sets the left.
     * 
     * @param queue the queue
     * @return true, if successful
     */
    boolean setLeft(HeapOut<V> queue);

    /**
     * Gets the left.
     * 
     * @return the left
     */
    HeapOut<V> getLeft();

    /**
     * Sets the right.
     * 
     * @param queue the queue
     * @return true, if successful
     */
    boolean setRight(HeapOut<V> queue);

    /**
     * Gets the right.
     * 
     * @return the right
     */
    HeapOut<V> getRight();
}
