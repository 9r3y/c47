package com.y3r9.c47.dog.swj.model.sort;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import cn.com.netis.dp.commons.lang.Constants;
import cn.com.netis.dp.commons.lang.NegativeArgumentException;
import com.y3r9.c47.dog.swj.model.sort.spi.SortQueue;

/**
 * The Class AbstractSortQueue.
 * 
 * @param <H> the generic type
 * @param <D> the generic type
 * 
 * @version 1.1
 * @see SortQueue
 * @since project 2.3
 */
public abstract class AbstractSortQueue<H, D> implements SortQueue<H, D> {

    @Override
    public final boolean hasSortedNext() {
        return emptyPartitions.isEmpty();
    }

    @Override
    public final Pair<H, D> next() {
        int partId = 0;
        Queue<Pair<H, D>> partition = partitionData.get(0);
        H presenter = partition.element().getLeft();

        // select the minimal one
        for (int i = 1; i < partitionCount; i++) {
            final Queue<Pair<H, D>> partData = partitionData.get(i);
            final H candidate = partData.peek().getLeft();
            if (selectCompare(candidate, presenter)) {
                partId = i;
                partition = partData;
                presenter = candidate;
            }
        }

        // process the minimal one
        if (partition.size() == 1) {
            emptyPartitions.add(partId);
        }
        cacheCount--;
        return partition.poll();
    }

    /**
     * Select compare.
     * 
     * @param candidate the candidate
     * @param presenter the presenter
     * @return true if candidate is <em>earlier than</em> presenter.
     */
    public abstract boolean selectCompare(H candidate, H presenter);

    @Override
    public final void addItem(final H header, final D data) {
        final int partId = getPartitionId(header);
        if (!isValidPartitionId(partId)) {
            return;
        }

        partitionData.get(partId).offer(Pair.of(header, data));
        emptyPartitions.remove(partId);
        cacheCount++;
    }

    /**
     * Gets the partition id.
     * 
     * @param header the header
     * @return the partition id
     */
    public abstract int getPartitionId(H header);

    /**
     * Gets the partition data.
     * 
     * @param partId the part id
     * @return the partition data
     */
    public final Queue<Pair<H, D>> getPartitionData(final int partId) {
        return isValidPartitionId(partId) ? partitionData.get(partId) : null;
    }

    /**
     * Checks if is valid partition id.
     * 
     * @param partId the part id
     * @return true, if is valid partition id
     */
    public final boolean isValidPartitionId(final int partId) {
        return partId >= 0 && partId < partitionCount;
    }

    @Override
    public final int getCacheSize() {
        return cacheCount;
    }

    @Override
    public final void setPartitionCount(final int value) {
        NegativeArgumentException.check(value, "partitionCount");
        if (value != partitionCount) {
            partitionCount = value;
            resetPartitionData();
        }
    }

    @Override
    public final int getPartitionCount() {
        return partitionCount;
    }

    @Override
    public final void report(final StringBuilder message) {
        message.append('[');
        for (final Queue<Pair<H, D>> entry : partitionData) {
            message.append(entry.size());
            message.append(Constants.CSV_ITEM_SEPARATOR);
        }
        message.append(']');
    }

    /**
     * Reset partition data.
     */
    private void resetPartitionData() {
        partitionData.clear();
        emptyPartitions.clear();
        for (int i = 0; i < partitionCount; i++) {
            partitionData.add(new ArrayDeque<Pair<H, D>>());
            emptyPartitions.add(i);
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("partitionCount=").append(partitionCount).append(", cacheCount=")
                .append(cacheCount).append(", hasSortedNext()=").append(hasSortedNext());
        return builder.toString();
    }

    /** The partition count. */
    private int partitionCount;

    /** The partition data. */
    private final transient List<Queue<Pair<H, D>>> partitionData;

    /** The empty partitions. */
    private final transient Set<Integer> emptyPartitions;

    /** The cache count. */
    private transient int cacheCount;

    {
        partitionData = new ArrayList<Queue<Pair<H, D>>>();
        emptyPartitions = new HashSet<Integer>();
    }
}
