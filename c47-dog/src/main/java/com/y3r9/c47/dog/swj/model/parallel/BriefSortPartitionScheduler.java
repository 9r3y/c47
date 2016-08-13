package com.y3r9.c47.dog.swj.model.parallel;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNodeManageable;

/**
 * Provide brief sort while consuming PartitionNode.
 * <p>
 * When consume, sort PartitionNodes selected by the next <em>SortSize</em> selectors with the
 * head-token, if PartitionNode is consuming by other thread then jump to the next selector,
 * until all <em>SortSize</em> PartitionNodes are selected. Try to consume the PartitionNode of
 * the minimum token. If it is lock or cache-queue is empty, try to consume the next minimum
 * token... until one PartitionNode consume some cache, return the consumed count.
 * </p>
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 * @since 3.3
 */
final class BriefSortPartitionScheduler<D, C, R> extends AbstractPartitionScheduler<D, C, R> {

    /** The constant DEFAULT_SORT_SIZE. */
    public static final int DEFAULT_SORT_SIZE = 4;

    @Override
    public PartitionNode<D, C, R> getPartition(final int selector,
            final PartitionNode<D, C, R> lastPartition) {
        List<PartitionNode<D, C, R>> sorts = new LinkedList<>();
        if (sortSize < 2) {
            super.getPartition(selector, lastPartition);
        }

        for (int i = 0; i < sortSize; i++) {
            final int nextSel = nextSelector(selector, i);
            PartitionNode<D, C, R> part = getPartitionNodeManager().getPartitionBySelector(nextSel);
            if (part == null || part.isConsuming()) {
                continue;
            }
            final long token = part.getCacheQueueHeadToken();
            if (token < 0) {
                continue;
            }
            sorts.add(part);
        }
        if (sorts.isEmpty()) {
            return null;
        }
        Collections.sort(sorts, comparator);
        return sorts.get(0);
    }

    /**
     * Next selector.
     *
     * @param selector the selector
     * @param step the step
     * @return the int
     */
    private int nextSelector(final int selector, final int step) {
        int result = selector + step;
        result = result < Integer.MAX_VALUE ? result : result - Integer.MAX_VALUE;
        return result;
    }

    /**
     * Sets sort size.
     *
     * @param value the value
     */
    public void setSortSize(final int value) {
        sortSize = value;
    }

    /**
     * Instantiates a new Brief sort partition select strategy.
     *
     * @param partMgr the part mgr
     */
    public BriefSortPartitionScheduler(final PartitionNodeManageable<D, C, R> partMgr) {
        super(partMgr);
    }

    /** The Sort size. */
    private int sortSize;

    /** The Comparator. */
    private Comparator<PartitionNode<D, C, R>> comparator;

    {
        sortSize = DEFAULT_SORT_SIZE;
        comparator = new PartitionComparator();
    }

}
