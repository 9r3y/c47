package com.y3r9.c47.dog.swj.model.sort;

import java.util.LinkedList;
import java.util.Queue;

import com.y3r9.c47.dog.swj.model.sort.spi.LongHeap;
import com.y3r9.c47.dog.swj.model.sort.spi.LongHeapOut;

/**
 * The Class MinimalLongHeap.
 * 
 * @param <V> the value type
 * @version 1.0
 * @see LongHeap
 * @since project 3.0
 */
public class MinimalLongHeap<V> implements LongHeap<V> {

    @Override
    public final boolean isReady() {
        return heapTop.isReady();
    }

    @Override
    public final long peekKey() {
        return heapTop.peekKey();
    }

    @Override
    public final V peekValue() {
        return heapTop.peekValue();
    }

    @Override
    public final void pop() {
        heapTop.pop();
    }

    @Override
    public final boolean buildHeap(final Queue<LongHeapOut<V>> queues) {
        // check input
        if (queues == null) {
            return false;
        }
        for (final LongHeapOut<V> queue : queues) {
            if (queue == null) {
                return false;
            }
        }

        // build heap recursively
        final Queue<LongHeapOut<V>> heap = buildHeapLevel(queues);
        if (heap.size() != 1) {
            return false;
        }

        // save the heap top
        heapTop = heap.remove();
        return true;
    }

    /**
     * Builds the heap level.
     * 
     * @param queues the queues
     * @return the list
     */
    private Queue<LongHeapOut<V>> buildHeapLevel(final Queue<LongHeapOut<V>> queues) {
        if (queues.size() == 1) {
            return queues;
        }

        final Queue<LongHeapOut<V>> result = new LinkedList<LongHeapOut<V>>();

        final int count = queues.size() % 2;
        for (int i = 0; i < count; i++) {
            final LongHeapNode<V> node = new LongHeapNode<V>();
            node.setLeft(queues.remove());
            node.setRight(queues.remove());
            result.add(node);
        }

        if (!queues.isEmpty()) {
            result.add(queues.remove());
        }

        return result;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("MinimalLongHeap [heapTop=").append(heapTop).append(']');
        return builder.toString();
    }

    /** The heap top. */
    private transient LongHeapOut<V> heapTop;

    {
        heapTop = new LongHeapDummyOut<V>();
    }
}
