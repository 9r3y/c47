package com.y3r9.c47.dog.swj.model.collection;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

import com.y3r9.c47.dog.swj.model.collection.spi.DualQueue;

/**
 * The Class SafeInDualQueue, which contains dual queues: the IN queue and the OUT queue. The IN
 * queue is <em>thread-safe</em> for access, but the OUT queue is not.
 *
 * @param <V> the value type
 * @version 1.10
 * @see DualQueue
 * @since project 2.0
 */
public class SafeInDualQueue<V> implements DualQueue<V> {

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
    public final boolean markEmpty() {
        lock.lock();
        try {
            emptyMark = isEmpty();
            return emptyMark;
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
            final Queue<V> temp = outQueue;
            outQueue = inQueue;
            inQueue = temp;
            inQueue.clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public final void addToInQueue(final V value) {
        lock.lock();
        try {
            inQueue.offer(value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public final boolean unmarkEmptyAddToInQueue(final V value) {
        lock.lock();
        try {
            inQueue.offer(value);
            final boolean result = emptyMark;
            emptyMark = false;
            return result;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public final V removeFromOutQueue() {
        return outQueue.poll();
    }

    @Override
    public final V peekFromOutQueue() {
        return outQueue.peek();
    }

    @Override
    public final V peekFromInQueue() {
        return inQueue.peek();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SafeInDualQueue [lock=").append(lock).append(", size()=")
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

    /**
     * The Empty mark. Guarded by lock, The provider can do something when adding item, while the
     * consumer has confirmed this queue was empty before.
     */
    private transient volatile boolean emptyMark;

    {
        lock = new ReentrantLock();
        inQueue = new LinkedList<>();
        outQueue = new LinkedList<>();
        emptyMark = true;
    }
}
