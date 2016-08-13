package com.y3r9.c47.dog.swj.model.parallel.spi;

import cn.com.netis.dp.commons.common.Configurable;

/**
 * The Interface PartitionManageable.
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 * @see  PartitionHandler, Partitioner
 * @since project 3.0
 */
public interface PartitionNodeManageable<D, C, R> extends PartitionScheduler<D, C, R>,
        Partitioner<D>, Configurable {

    /**
     * Gets the partitioner.
     *
     * @return the partitioner
     */
    Partitioner<D> getPartitioner();

    /**
     * Sets the partitioner.
     *
     * @param value the new partitioner
     */
    void setPartitioner(Partitioner<D> value);

    /**
     * Sets the partition count.
     *
     * @param value the value
     */
    void setPartitionCount(int value);

    /**
     * Gets the partition count.
     *
     * @return the partition count
     */
    int getPartitionCount();

    /**
     * Sets the use partition count.
     *
     * @param value the new use partition count
     */
    void setUsePartitionCount(int value);

    /**
     * Gets the use partition count.
     *
     * @return the use partition count
     */
    int getUsePartitionCount();

    /**
     * Sets the partition.
     *
     * @param index the index
     * @param part the part
     */
    void setPartition(int index, PartitionNode<D, C, R> part);

    /**
     * Gets the partition by index.
     *
     * @param index the index
     * @return the partition
     */
    PartitionNode<D, C, R> getPartitionByIndex(int index);

    /**
     * Gets the partition by selector.
     *
     * @param selector the selector used to select partition
     * @return the partition
     */
    PartitionNode<D, C, R> getPartitionBySelector(int selector);
}
