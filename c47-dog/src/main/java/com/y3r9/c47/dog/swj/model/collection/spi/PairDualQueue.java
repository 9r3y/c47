package com.y3r9.c47.dog.swj.model.collection.spi;

import org.apache.commons.lang3.tuple.Pair;

/**
 * The Interface PairDualQueue, which contains dual queues: the IN queue and the OUT queue. One of
 * IN queue and OUT queue should be <em>thread-safe</em>. This is decide by the implementation.
 * 
 * @param <H> the generic type
 * @param <D> the generic type
 * 
 * @version 1.5
 * @since project 2.0
 * @since 1.1 changes to generic
 * @since 1.2 renames to queue to IN queue and OUT queue
 * @since project 3.0 updates to 1.5
 * @since 1.5 renames to PairDualQueue
 */
public interface PairDualQueue<H, D> extends DualQueue<Pair<H, D>> {

    /**
     * Add data to the IN queue. This method is <em>thread-safe</em> or <em>not</em> is decided by
     * the implementation.
     * 
     * @param header the header
     * @param data the data
     */
    void addToInQueue(H header, D data);

    /**
     * Peek out queue for header.
     * 
     * @return the header
     * @since 1.4
     */
    H peekOutQueueForHeader();

    /**
     * Removes the out queue for data.
     * 
     * @return the data
     * @since 1.4
     */
    D removeOutQueueForData();
}
