package com.y3r9.c47.dog.swj.model.collection;

import java.util.AbstractQueue;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import cn.com.netis.dp.commons.lang.NonPositiveArgumentException;

/**
 * A bounded priority queue to solve TopN problem. This implementation use red-black tree rather
 * than heap in PriorityQueue. Use Google Guava's MinMaxPriorityQueue should get a better performance.
 *
 * @param <E> the type parameter
 * @version 1.0
 */
public final class BoundedPriorityQueue<E> extends AbstractQueue<E> {

    @Override
    public Iterator<E> iterator() {
        return treeSet.iterator();
    }

    @Override
    public int size() {
        return treeSet.size();
    }

    /**
     * Override to not throwing exception while queue is full.
     */
    @Override
    public boolean add(final E e) {
        return offer(e);
    }

    @Override
    public boolean offer(final E e) {
        if (treeSet.size() < capacity) {
            treeSet.add(e);
            return true;
        } else {
            if (last == null) {
                last = treeSet.last();
            }
            int comp;
            if (comparator != null) {
                comp = comparator.compare(e, last);
            } else {
                @SuppressWarnings("unchecked")
                final Comparable<? super E> key = (Comparable<? super E>) e;
                comp = key.compareTo(last);
            }
            if (comp < 0) {
                treeSet.pollLast();
                treeSet.add(e);
                last = null;
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void clear() {
        treeSet.clear();
        last = null;
    }

    @Override
    public E poll() {
        last = null;
        return treeSet.isEmpty() ? null : treeSet.pollFirst();
    }

    @Override
    public E peek() {
        return treeSet.isEmpty() ? null : treeSet.first();
    }

    /**
     * Instantiates a new Bounded priority queue.
     *
     * @param capacity the capacity
     */
    public BoundedPriorityQueue(final int capacity) {
        NonPositiveArgumentException.check(capacity, "capacity must be greater than 0");
        this.capacity = capacity;
        treeSet = new TreeSet<>();
    }

    /**
     * Instantiates a new Bounded priority queue.
     *
     * @param capacity the capacity
     * @param comparator the comparator
     */
    public BoundedPriorityQueue(final int capacity, final Comparator<? super E> comparator) {
        NonPositiveArgumentException.check(capacity, "capacity must be greater than 0");
        this.comparator = comparator;
        this.capacity = capacity;
        treeSet = new TreeSet<>(comparator);
    }

    /** The Tree set. */
    private final TreeSet<E> treeSet;

    /** The Comparator. */
    private Comparator<? super E> comparator;

    /** The Capacity. */
    private final int capacity;

    /** The last element while queue is full. */
    private E last;
}
