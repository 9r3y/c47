package com.y3r9.c47.dog.swj.model.collection.spi;

import cn.com.netis.dp.commons.common.Threadable;

/**
 * The Interface PairDualQueueThread. This thread contains a dual queue instance as the cache
 * buffer, whose IN queue or OUT queue should be <em>thread-safe</em>. The same time, when the dual
 * queue is full, its behavior of blocking or dropping the new incoming data is not defined by this
 * interface. The implementation could decide its usages.
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
 * </...>
 * 
 * </pre>
 * 
 * @param <H> the generic header type
 * @param <D> the generic data type
 * 
 * @version 2.10
 * @see Threadable
 * @since project 2.0
 * @since 1.3 changes to generic
 * @since 1.7 adds generic type H
 * @since project 3.0 updates to 2.10
 * @since 2.10 extends Threadable
 * @since 2.10 renames to PairDualQueueThread
 */
public interface PairDualQueueThread<H, D> extends Threadable {

    /**
     * Gets the cache queue size.
     * 
     * @return the cache queue size
     * @since 1.5
     */
    int getCacheQueueSize();

    /**
     * Gets the cache pair queue.
     * 
     * @return the cache pair queue
     * @since 1.8
     */
    PairDualQueue<H, D> getCachePairQueue();

    /**
     * Adds the item.
     * 
     * @param header the header
     * @param data the data
     */
    void addItem(H header, D data);
}
