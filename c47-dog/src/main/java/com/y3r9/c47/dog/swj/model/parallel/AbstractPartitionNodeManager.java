package com.y3r9.c47.dog.swj.model.parallel;

import java.util.ArrayList;
import java.util.function.BiFunction;

import org.apache.commons.configuration.Configuration;

import cn.com.netis.dp.commons.common.ConfigHint;
import cn.com.netis.dp.commons.lang.OutOfRangeArgumentException;
import com.y3r9.c47.dog.swj.config.key.PollingKey;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNodeManageable;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionScheduler;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionSchedulerType;
import com.y3r9.c47.dog.swj.model.parallel.spi.Partitioner;

/**
 * The Class AbstractPartitionNodeManager.
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.2
 * @see PartitionNodeManageable
 * @since project 3.0
 */
public abstract class AbstractPartitionNodeManager<D, C, R> implements
        PartitionNodeManageable<D, C, R> {

    @Override
    public PartitionNode<D, C, R> getPartition(final int selector,
            final PartitionNode<D, C, R> lastConsumed) {
        return scheduler.getPartition(selector, lastConsumed);
    }

    @Override
    public boolean needLockPartition() {
        return scheduler.needLockPartition();
    }

    @Override
    public final void dispatchItem(final long token, final D data) {
        scheduler.dispatchItem(token, data);
    }

    @Override
    public final Partitioner<D> getPartitioner() {
        return partitioner;
    }

    @Override
    public final void setPartitioner(final Partitioner<D> value) {
        partitioner = value;
    }

    @Override
    public final void setPartitionCount(final int value) {
        if (value < 1) {
            throw new IllegalArgumentException("partitionCount must >= 1");
        }

        final int size = normalizePartitionCount(value);

        // allocate pool
        pool = new ArrayList<>(size);
        /**
         * Because selector may out of control, so it needs to initialize array for the the full
         * partitionCount, not only the usePartitionCount.
         */
        for (int i = 0; i < size; i++) {
            pool.add(i, null);
        }

        // update related value
        updateUsePartitionCount();
    }

    /**
     * Normalize partition count.
     *
     * @param partCount the part count
     * @return the size
     */
    protected abstract int normalizePartitionCount(int partCount);

    @Override
    public final int getUsePartitionCount() {
        return usePartitionCount;
    }

    @Override
    public final void setUsePartitionCount(final int value) {
        OutOfRangeArgumentException.check(value, 1, getPartitionCount() + 1, "usePartitionCount");
        usePartitionCount = value;
        updateUsePartitionCount();
    }

    /**
     * Update use partition count.
     */
    private void updateUsePartitionCount() {
        if (usePartitionCount > getPartitionCount()) {
            usePartitionCount = getPartitionCount();
        }
    }

    @Override
    public final void setPartition(final int index, final PartitionNode<D, C, R> part) {
        pool.set(index, part);
    }

    @Override
    public final PartitionNode<D, C, R> getPartitionByIndex(final int index) {
        return pool.get(index);
    }

    @Override
    public PartitionNode<D, C, R> getPartitionBySelector(final int selector) {
        return pool.get(selectorToIndex(selector));
    }

    /**
     * Selector to index.
     *
     * @param selector the selector
     * @return the index
     */
    protected abstract int selectorToIndex(final int selector);

    @Override
    public final int getPartSelector(final long token, final D data) {
        return partitioner.getPartSelector(token, data);
    }

    @Override
    public void updateConfiguration(final Configuration config, final int hint) {
        /**
         * Can be extended.
         */
        if (config == null) {
            return;
        }
        if (ConfigHint.NATIVE_FILE == hint || ConfigHint.CLI_OVERRIDE == hint) {
            final PartitionSchedulerType type = PartitionSchedulerType.valueOf(config.getString(
                    PollingKey.partitionScheduler.name(),
                    PartitionSchedulerType.selector.name()).toLowerCase());
            switch (type) {
            case selector:
                break;
            case briefsort:
                final int briefSortSize = config.getInt(PollingKey.briefSortSize.name(),
                        BriefSortPartitionScheduler.DEFAULT_SORT_SIZE);
                BriefSortPartitionScheduler<D, C, R> csm = new BriefSortPartitionScheduler<>(this);
                csm.setSortSize(briefSortSize);
                scheduler = csm;
                break;
            case priority:
                scheduler = new PriorityPartitionScheduler<>(this);
                break;
            case queue:
                scheduler = new QueuePartitionScheduler<>(this);
                break;
            default:
                throw new IllegalStateException("Unreachable code.");
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("usePartitionCount=").append(usePartitionCount)
                .append(", getPartitionCount()=").append(getPartitionCount())
                .append(", partitioner=").append(partitioner);
        return builder.toString();
    }

    /** The pool. DO NOT ADD to toString(). */
    private transient ArrayList<PartitionNode<D, C, R>> pool;

    /** The partitioner. */
    private Partitioner<D> partitioner;

    /** The use partition count. */
    private int usePartitionCount;

    /** The Scheduler. */
    private PartitionScheduler<D, C, R> scheduler;

    {
        scheduler = new SelectorPartitionScheduler<>(this);
    }
}
