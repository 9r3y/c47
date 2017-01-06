package com.y3r9.c47.dog.swj2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The class Partition.
 *
 * @version 1.0
 */
final class Partition<T> {

    synchronized void add(T data) {
        inQueue.add(data);
    }

    synchronized void fetch() {
        Queue<T> tmp = inQueue;
        inQueue = outQueue;
        outQueue = tmp;
    }

    T get() {
        return outQueue.poll();
    }

    private Queue<T> inQueue = new LinkedList<T>();

    private Queue<T> outQueue = new LinkedList<T>();

    private Lock syn = new ReentrantLock();

}
