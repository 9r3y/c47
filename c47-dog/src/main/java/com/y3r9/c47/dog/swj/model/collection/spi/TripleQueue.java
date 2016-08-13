package com.y3r9.c47.dog.swj.model.collection.spi;

import org.apache.commons.lang3.tuple.Pair;

import com.y3r9.c47.dog.swj.polling.spi.SizeProc;

/**
 * The Interface TripleQueue, which contains three queues: the IN queue, the OUT queue, and the MID
 * Queue. Only the MID queue <em>thread-safe</em>, both the IN queue and the OUT queue are not.
 * 
 * The IN queue and MID queue consist of the WAITING queue. And queue size control should apply on
 * the WAITING queue.
 * 
 * The MID queue and OUT queue consist of the PROCESSING queue.
 * 
 * @param <H> the generic type
 * @param <D> the generic type
 * 
 * @version 1.2
 * @see SizeProc
 * @since project 2.0
 * @since project 3.0 updates to 1.2
 * @since 1.2 extends SizeProc
 */
public interface TripleQueue<H, D> extends SizeProc {

    /**
     * Size, which is sum of IN queue, MID queue, and OUT queue.
     * 
     * @return the queue size
     */
    @Override
    int size();

    /**
     * Checks if is waiting queue empty. Both IN queue and MID queue are empty, it return true.
     * 
     * @return true, if is waiting queue empty
     */
    boolean isWaitingQueueEmpty();

    /**
     * Gets the waiting queue size, which is sum of IN queue and OUT queue.
     * 
     * @return the size
     */
    int getWaitingQueueSize();

    /**
     * Checks if is process queue empty.
     * 
     * @return true, if is process queue empty
     */
    boolean isProcessQueueEmpty();

    /**
     * Gets the processing queue size.
     * 
     * @return the processing queue size
     */
    int getProcessingQueueSize();

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
     * Gets the mid queue size.
     * 
     * @return the mid queue size
     */
    int getMidQueueSize();

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
     * Checks if is mid queue empty.
     * 
     * @return true, if is mid queue empty
     * @since 1.1
     */
    boolean isMidQueueEmpty();

    /**
     * Safe drain data from the IN queue to MID queue, which is <em>thread-safe</em> to enter this
     * method.
     */
    void safeDrainInQueueToMid();

    /**
     * Safe drain data from the MID queue to out queue, which is <em>thread-safe</em> to enter this
     * method.
     */
    void safeDrainMidQueueToOut();

    /**
     * Add data to the IN queue. This method is <em>not</em> <em>thread-safe</em>.
     * 
     * @param header the header
     * @param data the data
     */
    void addToInQueue(H header, D data);

    /**
     * Removes the data from the OUT queue. This method is <em>not</em> <em>thread-safe</em>.
     * 
     * @return the pair
     */
    Pair<H, D> removeFromOutQueue();
}
