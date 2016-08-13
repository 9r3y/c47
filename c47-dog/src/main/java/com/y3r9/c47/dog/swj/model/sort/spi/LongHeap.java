package com.y3r9.c47.dog.swj.model.sort.spi;

import java.util.Queue;

/**
 * The Interface LongHeap.
 * 
 * @param <V> the value type
 * @version 1.0
 * @see LongHeapOut
 * @since project 3.0
 */
public interface LongHeap<V> extends LongHeapOut<V> {

    /**
     * Builds the heap.
     * 
     * @param queues the queues
     * @return true, if successful
     */
    boolean buildHeap(Queue<LongHeapOut<V>> queues);
}
