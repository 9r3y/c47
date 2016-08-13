package com.y3r9.c47.dog.swj.model.sort;

import java.util.LinkedList;
import java.util.Queue;

import com.y3r9.c47.dog.swj.model.sort.spi.Heap;
import com.y3r9.c47.dog.swj.model.sort.spi.HeapOut;

/**
 * The Class MinimalHeap.
 * 
 * @param <V> the value type
 * @version 1.0
 * @see Heap
 * @since project 3.0
 */
public class MinimalHeap<V> implements Heap<V> {

    @Override
    public boolean hasNext() {
        return heapTop.hasNext();
    }

    @Override
    public final boolean isNextReady() {
        return heapTop.isNextReady();
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
    public V pop() {
        return heapTop.pop();
    }

    @Override
    public boolean buildHeap(final Queue<? extends HeapOut<V>> queues) {
        // check input
        if (queues == null) {
            return false;
        }
        if (queues.isEmpty()) {
            return false;
        }
        for (final HeapOut<V> queue : queues) {
            if (queue == null) {
                return false;
            }
        }

        // copy queue to prevent clear input queues
        final Queue<HeapOut<V>> firstLevel = new LinkedList<HeapOut<V>>();
        firstLevel.addAll(queues);

        // build heap recursively
        final Queue<? extends HeapOut<V>> heap = buildHeapLevel(firstLevel);

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
    private Queue<? extends HeapOut<V>> buildHeapLevel(
            final Queue<? extends HeapOut<V>> queues) {
        if (queues.size() == 1) {
            return queues;
        }

        final Queue<HeapOut<V>> result = new LinkedList<HeapOut<V>>();

        final int count = queues.size() / 2;
        for (int i = 0; i < count; i++) {
            final HeapNode<V> node = new HeapNode<>();
            node.setLeft(queues.remove());
            node.setRight(queues.remove());
            result.add(node);
        }

        if (!queues.isEmpty()) {
            result.add(queues.remove());
        }

        // build recursively
        return buildHeapLevel(result);
    }

    /** The heap top. */
    private transient HeapOut<V> heapTop;

    {
        heapTop = new HeapDummyOut<V>();
    }
}
