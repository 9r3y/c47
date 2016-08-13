package com.y3r9.c47.dog.swj.model.parallel;

/**
 * The Class ModPartitionNodeManager.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @see ModPartitionNodeManager
 * @since project 3.0
 */
public final class ModPartitionNodeManager<D, C, R> extends AbstractPartitionNodeManager<D, C, R> {

    @Override
    public int getPartitionCount() {
        return partitionCount;
    }

    @Override
    protected int normalizePartitionCount(final int partCount) {
        partitionCount = partCount;
        return partCount;
    }

    @Override
    protected int selectorToIndex(final int selector) {
        return selector % partitionCount;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ModPartitionNodeManager [").append(super.toString()).append("]");
        return builder.toString();
    }

    /** The partition count. */
    private int partitionCount;
}
