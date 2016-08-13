package com.y3r9.c47.dog.swj.model.collection.spi;

import com.y3r9.c47.dog.swj.polling.spi.SizeProc;

/**
 * The Interface PairDualQueue, which contains dual queues: the IN queue and the OUT queue. One of
 * IN queue and OUT queue should be <em>thread-safe</em>. This is decide by the implementation.
 * 
 * @param <V> the value type
 * @version 1.7
 * @see SizeProc
 * @since project 2.0
 * @since 1.1 changes to generic
 * @since 1.2 renames to queue to IN queue and OUT queue
 * @since project 3.0 updates to 1.5
 * @since 1.5 changes to single value
 * @since 1.5 extends SizeProc
 */
public interface DualQueue<V> extends SizeProc {

    /**
     * Checks if is empty. Both IN queue and OUT queue are empty, it return true.
     * 
     * @return true, if is empty
     */
    boolean isEmpty();

    /**
     * Confirm is empty. Thread-safe.
     * 
     * @return true, if successful
     * @since 1.3
     */
    boolean confirmIsEmpty();

    /**
     * Size, which is sum of IN queue and OUT queue.
     * 
     * @return the size
     */
    @Override
    int size();

    /**
     * Gets the in queue size.
     * 
     * @return the in queue size
     */
    int getInQueueSize();

    /**
     * Gets the out queue size.
     * 
     * @return the out queue size
     */
    int getOutQueueSize();

    /**
     * Checks if is in queue empty.
     * 
     * @return true, if is in queue empty
     */
    boolean isInQueueEmpty();

    /**
     * Checks if is out queue empty.
     * 
     * @return true, if is out queue empty
     */
    boolean isOutQueueEmpty();

    /**
     * Safe drain data from the IN queue to OUT queue, which is <em>thread-safe</em> to enter this
     * method.
     */
    void safeDrainInQueueToOut();

    /**
     * Add data to the IN queue. This method is <em>thread-safe</em> or <em>not</em> is decided by
     * the implementation.
     *
     * @param value the value
     */
    void addToInQueue(V value);

    /**
     * Removes the data from the OUT queue. This method is <em>thread-safe</em> or <em>not</em> is
     * decided by the implementation.
     * 
     * @return the value
     */
    V removeFromOutQueue();

    /**
     * Peek data from the OUT queue. This method is <em>thread-safe</em> or <em>not</em> is decided
     * by the implementation.
     * 
     * @return the v
     */
    V peekFromOutQueue();

    /**
     * Peek data from the IN queue. This method is <em>thread-safe</em> or <em>not</em> is decided
     *
     * @return the v
     */
    V peekFromInQueue();

    /**
     * Mark the empty mark if queue is empty. Thread safe for InQueue.
     *
     * @return the boolean true if empty mark is marked true.
     * @since 1.7
     */
    boolean markEmpty();

    /**
     * Unmark empty add to in queue.
     *
     * @param value the value
     * @return true, if unmark empty add to in queue
     * @since 1.7
     */
    boolean unmarkEmptyAddToInQueue(final V value);
}
