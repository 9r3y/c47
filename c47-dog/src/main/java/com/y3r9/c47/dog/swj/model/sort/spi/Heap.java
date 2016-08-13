package com.y3r9.c47.dog.swj.model.sort.spi;

import java.util.Queue;

/**
 * The Interface Heap.
 * 
 * @param <V> the value type
 * @version 1.0
 * @see HeapOut
 * @since project 3.0
 */
public interface Heap<V> extends HeapOut<V> {

    /**
     * Builds the heap.
     * 
     * @param queues the queues
     * @return true, if successful
     */
    boolean buildHeap(Queue<? extends HeapOut<V>> queues);
}
