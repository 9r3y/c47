package com.y3r9.c47.dog.swj.model.parallel;

import java.util.Comparator;

import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNodeManageable;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionScheduler;

/**
 * Abstract implementation which provide PartitionNode scheduling by selector.
 * <pre>Dispatch</pre>
 * SplitNode's token and data -> selector(by Partitioner) ->
 * PartitionNode's index(by PartitionNodeManager)
 * <pre>Consume</pre>
 * WorkNode's selector -> PartitionNode's index(by PartitionNodeManager)
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 */
abstract class AbstractPartitionScheduler<D, C, R> implements PartitionScheduler<D, C, R> {

    @Override
    public void dispatchItem(final long token, final D data) {
        /**
         * Use Partitioner to convert token to selector.
         */
        final int selector = getPartitionNodeManager().getPartSelector(token, data);
        /**
         * Called by worker because only the splitNode maintains the partitions.
         */
        final PartitionNode<D, C, R> part = getPartitionNodeManager()
                .getPartitionBySelector(selector);
        /**
         * Because data is controlled logically, so the selector from the partitioner with respect
         * to the data should always matches a valid partition. So, the partition should never be
         * NULL, and no data will be dropped.
         */
        part.addItem(token, data);
    }

    @Override
    public PartitionNode<D, C, R> getPartition(final int selector,
            final PartitionNode<D, C, R> lastConsumed) {
        /**
         * Called by worker because only the splitNode maintains the partitions.
         */
        return partMgr.getPartitionBySelector(selector);
    }

    @Override
    public boolean needLockPartition() {
        return true;
    }

    /**
     * Gets partition node manager.
     *
     * @return the partition manager
     */
    protected PartitionNodeManageable<D, C, R> getPartitionNodeManager() {
        return partMgr;
    }

    /**
     * Instantiates a new Abstract partition producer.
     *
     * @param partMgr the partition manager
     */
    protected AbstractPartitionScheduler(final PartitionNodeManageable<D, C, R> partMgr) {
        this.partMgr = partMgr;
    }

    /** The Part mgr. */
    private final PartitionNodeManageable<D, C, R> partMgr;

    /**
     * The class Partition comparator.
     *
     * @version 1.0
     */
    protected class PartitionComparator implements Comparator<PartitionNode<D, C, R>> {

        @Override
        public int compare(final PartitionNode<D, C, R> o1, final PartitionNode<D, C, R> o2) {
            final long token1 = o1.getCacheQueueHeadToken();
            final long token2 = o2.getCacheQueueHeadToken();
            return token1 > token2 ? 1 : token1 < token2 ? -1 : 0;
        }
    }
}
