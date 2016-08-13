package com.y3r9.c47.dog.swj.model.collection;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

import com.y3r9.c47.dog.swj.model.collection.spi.DualQueue;
import com.y3r9.c47.dog.swj.model.collection.spi.PairDualQueue;

/**
 * The Class SafeInPairDualQueue, which contains dual queues: the IN queue and the OUT queue. The
 * OUT queue is <em>thread-safe</em> for access, but the IN queue is not.
 *
 * @param <V> the generic type
 *
 * @version 1.6
 * @see PairDualQueue
 * @since project 1.0
 */
public class SafeOutDualQueue<V> implements DualQueue<V> {

    @Override
    public final boolean isEmpty() {
        // use function for violate like effect
        return isInQueueEmpty() && isOutQueueEmpty();
    }

    @Override
    public final boolean confirmIsEmpty() {
        lock.lock();
        try {
            return isEmpty();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public final int size() {
        // use function for violate like effect
        return getInQueueSize() + getOutQueueSize();
    }

    @Override
    public final int getInQueueSize() {
        return inQueue.size();
    }

    @Override
    public final int getOutQueueSize() {
        return outQueue.size();
    }

    @Override
    public final boolean isInQueueEmpty() {
        return inQueue.isEmpty();
    }

    @Override
    public final boolean isOutQueueEmpty() {
        return outQueue.isEmpty();
    }

    @Override
    public final void safeDrainInQueueToOut() {
        lock.lock();
        try {
            outQueue.addAll(inQueue);
            inQueue.clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public final void addToInQueue(final V value) {
        inQueue.offer(value);
    }

    @Override
    public final V removeFromOutQueue() {
        lock.lock();
        try {
            return outQueue.poll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public final V peekFromOutQueue() {
        lock.lock();
        try {
            return outQueue.peek();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public final V peekFromInQueue() {
        return inQueue.peek();
    }

    @Override
    public boolean markEmpty() {
        throw new UnsupportedOperationException("SafeOutDualQueue#markEmpty");
    }

    @Override
    public boolean unmarkEmptyAddToInQueue(final V value) {
        throw new UnsupportedOperationException("SafeOutDualQueue#unmarkEmptyAddToInQueue");
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SafeOutDualQueue [lock=").append(lock).append(", size()=")
                .append(size()).append(", getInQueueSize()=").append(getInQueueSize())
                .append(", getOutQueueSize()=").append(getOutQueueSize()).append("]");
        return builder.toString();
    }

    /** The lock. */
    private final transient ReentrantLock lock;

    /** The IN queue. */
    private transient Queue<V> inQueue;

    /** The OUT queue. */
    private transient Queue<V> outQueue;

    {
        lock = new ReentrantLock();
        inQueue = new LinkedList<>();
        outQueue = new LinkedList<>();
    }

}
