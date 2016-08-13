package com.y3r9.c47.dog.swj.model.parallel;

import java.util.function.BiFunction;

import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNodeManageable;

/**
 * Partition scheduler for partition bind case.
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 */
final class BindPartitionScheduler<D, C, R> extends AbstractPartitionScheduler<D, C, R> {

    @Override
    public void dispatchItem(final long token, final D data) {
        partitionNode.addItem(token, data);
    }

    @Override
    public PartitionNode<D, C, R> getPartition(final int selector,
            final PartitionNode<D, C, R> lastConsumed) {
        return partitionNode;
    }

    /**
     * Instantiates a new Abstract partition producer.
     *
     * @param partMgr the partition manager
     * @param index the partition index
     */
    protected BindPartitionScheduler(final PartitionNodeManageable<D, C, R> partMgr,
            final int index) {
        super(partMgr);
        partitionNode = partMgr.getPartitionByIndex(index);
    }

    /** The Partition node. */
    private PartitionNode<D, C, R> partitionNode;
}
