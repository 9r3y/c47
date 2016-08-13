package com.y3r9.c47.dog.swj.model.parallel.spi;

/**
 * The interface PartitionScheduler. All implements should schedule how to add item to
 * the PartitionNode (by SplitNode), and which PartitionNode to consume (by WorkNode).
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 * @since 3.3
 */
public interface PartitionScheduler<D, C, R> {

    /**
     * Dispatch item.
     *
     * @param token the token
     * @param data the data
     */
    void dispatchItem(long token, D data);

    /**
     * Gets partition.
     *
     * @param selector the selector
     * @param lastConsumed the last consumed
     * @return the partition to consume, <em>could be null.</em>
     */
    PartitionNode<D, C, R> getPartition(final int selector, final PartitionNode<D, C, R> lastConsumed);

    /**
     * Need lock partition boolean.
     *
     * @return the boolean
     */
    boolean needLockPartition();

}
