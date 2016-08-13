package com.y3r9.c47.dog.swj.model.top;

import java.util.Comparator;

/**
 * The type Priority queue.
 *
 * @param <T> the type parameter
 */
final class MinHeap<T> {

    /**
     * Add element with overflow.
     *
     * @param element the element
     * @return true, if update heap
     */
    public boolean insertWithOverflow(final T element) {
        if (size < maxSize) {
            // add directly
            size++;
            heap[size - 1] = element;
            upHeap();
            return true;
        } else if (size > 0 && comparator.compare(element, heap[0]) > 0) {
            // update heap top
            heap[0] = element;
            updateTop();
            return true;
        } else {
            // do not update heap
            return false;
        }
    }

    /**
     * Get heap array.
     *
     * @return the heap.
     */
    public T[] getHeapArray() {
        return heap;
    }

    /**
     * Pop element.
     *
     * @return the top element
     */
    public T pop() {
        if (size > 0) {
            final T result = heap[0];
            heap[0] = heap[size - 1];
            heap[size - 1] = null;
            size--;
            downHeap();
            return result;
        } else {
            return null;
        }
    }

    /**
     * Clear the queue.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    /**
     * Size int.
     *
     * @return the int
     */
    public int size() {
        return size;
    }

    /**
     * Update top element.
     *
     * @return the element
     */
    private T updateTop() {
        downHeap();
        return heap[0];
    }

    /**
     * Down heap.
     */
    private void downHeap() {
        int i = 0;
        final T node = heap[i];

        int left = (i << 1) + 1;
        int right = left + 1;
        if (right < size && comparator.compare(heap[right], heap[left]) < 0) {
            left = right;
        }

        while (left < size && comparator.compare(heap[left], node) < 0) {
            heap[i] = heap[left];
            i = left;
            left = (i << 1) + 1;
            right = left + 1;
            if (right < size && comparator.compare(heap[right], heap[left]) < 0) {
                left = right;
            }
        }
        heap[i] = node;
    }

    /**
     * Up heap.
     */
    private void upHeap() {
        int i = size - 1;
        final T node = heap[i]; // the bottom element
        int parent = (i - 1) >> 1;
        while (parent >= 0 && comparator.compare(node, heap[parent]) < 0) {
            // swap element
            heap[i] = heap[parent];
            i = parent;
            parent = (i - 1) >> 1;
        }
        heap[i] = node;
    }

    /**
     * Instantiates a new Priority queue.
     *
     * @param maxSize the max size
     * @param comparator the comparator
     */
    @SuppressWarnings("unchecked")
    public MinHeap(final int maxSize, final Comparator<T> comparator) {
        this.maxSize = maxSize;
        this.comparator = comparator;
        this.heap = (T[]) new Object[maxSize];
    }

    /** The size. */
    private int size;

    /** The Max size. */
    private final int maxSize;

    /** The Comparator. */
    private final Comparator<T> comparator;

    /** The Heap. */
    private final T[] heap;
}
