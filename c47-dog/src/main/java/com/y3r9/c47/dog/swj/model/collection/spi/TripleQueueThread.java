package com.y3r9.c47.dog.swj.model.collection.spi;

import org.apache.commons.lang3.tuple.Pair;

import cn.com.netis.dp.commons.common.Threadable;

/**
 * The Interface TripleQueueThread. This thread contains a triple queue instance as the cache
 * buffer. When the WAITING queue in the triple queue is full, its behavior of blocking or dropping
 * the new incoming data is not defined by this interface. The implementation could decide its
 * usages.
 * 
 * <pre>
 * XML example for configuration:
 * 
 * <...>
 *   <idleRetryCount>4</idleRetryCount>
 *   <ideaSleepNano>1</ideaSleepNano>
 *   <cacheSize>200000</cacheSize>
 *   <overSizeRetryCount>10000</overSizeRetryCount>
 *   <overSizeBlockingNano>1</overSizeBlockingNano>
 *   <dropLogTickPacket>100000</dropLogTickPacket>
 *   <pushToProcessSize>20</pushToProcessSize>
 *   <pushToProcessMilli>1000</pushToProcessMilli>
 * </...>
 * 
 * </pre>
 * 
 * @param <H> the generic header type
 * @param <D> the generic data type
 * 
 * @version 2.5
 * @see Threadable
 * @since project 2.0
 * @since project 3.0 updates to 2.5
 * @since 2.5 extends Threadable
 */
public interface TripleQueueThread<H, D> extends Threadable {

    /**
     * Gets the cache queue size.
     * 
     * @return the cache queue size
     * @since 1.5
     */
    int getCacheQueueSize();

    /**
     * Gets the cache queue.
     * 
     * @return the cache queue
     */
    TripleQueue<H, D> getCacheQueue();

    /**
     * Adds the item.
     * 
     * @param time the time
     * @param header the header
     * @param data the data
     * @return <code>true</code> if successful added, <code>false</code> if dropped.
     * @since 1.1 add return value
     * @since 1.2 add time to push up
     */
    boolean addItem(long time, H header, D data);

    /**
     * Flush. Drain up all data in the queue.
     * 
     * @since 1.3
     */
    void flush();

    /**
     * Checks for next item.
     * 
     * @return true, if successful
     */
    boolean hasNextItem();

    /**
     * Removes the item.
     * 
     * @return the pair
     */
    Pair<H, D> removeItem();

    /**
     * Trigger push to process.
     * 
     * @since 1.7
     */
    void triggerPushToProcess();
}
