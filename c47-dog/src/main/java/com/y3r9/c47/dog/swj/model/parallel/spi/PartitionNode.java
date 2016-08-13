package com.y3r9.c47.dog.swj.model.parallel.spi;

import cn.com.netis.dp.commons.common.Configurable;
import cn.com.netis.dp.commons.common.packet.PartInfo;

/**
 * The Interface PartitionNode.
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 * @see PartitionHandler, PartInfo
 * @since project 3.0
 */
public interface PartitionNode<D, C, R> extends PartitionHandler<D, C, R>, PartInfo, Configurable {

    /**
     * Gets the partition name.
     *
     * @return the partition name
     */
    String getPartitionName();

    /**
     * Gets the partition context.
     *
     * @return the partition context
     */
    C getContext();

    /**
     * Sets the partition context.
     *
     * @param value the new partition context
     */
    void setContext(C value);

    /**
     * Adds the item. For the producer thread to put item.
     *
     * @param token the token
     * @param data the data
     */
    void addItem(long token, D data);

    /**
     * Mark empty. See SafeInDualQueue.markEmpty().
     *
     * @return the boolean
     */
    boolean markEmpty();

    /**
     * Unmark empty add item. See SafeInDualQueue.unmarkEmptyAddItem().
     *
     * @param token the token
     * @param data the data
     * @return the boolean
     */
    boolean unmarkEmptyAddItem(long token, D data);

    /**
     * Gets the cache queue size.
     *
     * @return the cache queue size
     */
    int getCacheQueueSize();

    /**
     * Gets cache queue head token. If cache queue is empty return -1.
     *
     * @return the head token
     */
    long getCacheQueueHeadToken();

    /**
     * Is consuming.
     *
     * @return the boolean
     */
    boolean isConsuming();
}
