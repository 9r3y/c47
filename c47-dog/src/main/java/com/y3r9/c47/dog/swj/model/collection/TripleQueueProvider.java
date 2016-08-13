package com.y3r9.c47.dog.swj.model.collection;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.tuple.Pair;

import com.y3r9.c47.dog.swj.model.collection.spi.TripleQueue;

/**
 * The Class TripleQueueProvider, which contains three queues: the IN queue, the OUT queue, and the
 * MID Queue. Only the MID queue <em>thread-safe</em>, both the IN queue and the OUT queue are not.
 * 
 * @param <H> the generic type
 * @param <D> the generic type
 * 
 * @version 1.5
 * @see TripleQueue
 * @since project 2.0
 */
public final class TripleQueueProvider<H, D> implements TripleQueue<H, D> {

    @Override
    public int size() {
        // use function for violate like effect
        return getInQueueSize() + getMidQueueSize() + getOutQueueSize();
    }

    @Override
    public boolean isWaitingQueueEmpty() {
        // use function for violate like effect
        return isInQueueEmpty() && isMidQueueEmpty();
    }

    @Override
    public int getWaitingQueueSize() {
        // use function for violate like effect
        return getInQueueSize() + getMidQueueSize();
    }

    @Override
    public boolean isProcessQueueEmpty() {
        // use function for violate like effect
        return isOutQueueEmpty() && isMidQueueEmpty();
    }

    @Override
    public int getProcessingQueueSize() {
        // use function for violate like effect
        return getOutQueueSize() + getMidQueueSize();
    }

    @Override
    public int getInQueueSize() {
        return inQueue.size();
    }

    @Override
    public int getOutQueueSize() {
        return outQueue.size();
    }

    @Override
    public int getMidQueueSize() {
        return midQueue.size();
    }

    @Override
    public boolean isInQueueEmpty() {
        return inQueue.isEmpty();
    }

    @Override
    public boolean isOutQueueEmpty() {
        return outQueue.isEmpty();
    }

    @Override
    public boolean isMidQueueEmpty() {
        return midQueue.isEmpty();
    }

    @Override
    public void safeDrainInQueueToMid() {
        lock.lock();
        try {
            midQueue.addAll(inQueue);
            inQueue.clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void safeDrainMidQueueToOut() {
        lock.lock();
        try {
            // exchange the MID and OUT queue
            final Queue<Pair<H, D>> temp = outQueue;
            outQueue = midQueue;
            midQueue = temp;
            // make sure the MID queue is empty
            midQueue.clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addToInQueue(final H header, final D data) {
        inQueue.offer(Pair.of(header, data));
    }

    @Override
    public Pair<H, D> removeFromOutQueue() {
        if (outQueue.isEmpty()) {
            return null;
        }
        return outQueue.poll();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TripleQueueProvider [lock=").append(lock)
                .append(", isWaitingQueueEmpty()=").append(isWaitingQueueEmpty())
                .append(", getWaitingQueueSize()=").append(getWaitingQueueSize())
                .append(", getInQueueSize()=").append(getInQueueSize())
                .append(", getOutQueueSize()=").append(getOutQueueSize())
                .append(", getMidQueueSize()=").append(getMidQueueSize()).append("]");
        return builder.toString();
    }

    /** The lock. */
    private final transient ReentrantLock lock;

    /** The IN queue. */
    private final transient Queue<Pair<H, D>> inQueue;

    /** The MID queue. */
    private transient Queue<Pair<H, D>> midQueue;

    /** The OUT queue. */
    private transient Queue<Pair<H, D>> outQueue;

    {
        lock = new ReentrantLock();
        inQueue = new LinkedList<Pair<H, D>>();
        midQueue = new LinkedList<Pair<H, D>>();
        outQueue = new LinkedList<Pair<H, D>>();
    }
}
