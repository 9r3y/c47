package com.y3r9.c47.dog.swj.model.parallel;

import cn.com.netis.dp.commons.lang.Constants;
import com.y3r9.c47.dog.swj.value.Power2Utils;

/**
 * The Class MaskPartitionNodeManager.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @see ModPartitionNodeManager
 * @since project 3.0
 */
public final class MaskPartitionNodeManager<D, C, R> extends AbstractPartitionNodeManager<D, C, R> {

    @Override
    public int getPartitionCount() {
        return indexMask + 1;
    }

    @Override
    protected int normalizePartitionCount(final int partCount) {
        final int result = Power2Utils.power2Ceil(partCount, Constants.MIN_PARTITION_COUNT,
                Constants.MAX_PARTITION_COUNT);

        // update to new value
        indexMask = result - 1;
        return result;
    }

    @Override
    protected int selectorToIndex(final int selector) {
        return selector & indexMask;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("MaskPartitionNodeManager [").append(super.toString())
                .append(", indexMask=").append(indexMask).append("]");
        return builder.toString();
    }

    /** The index mask. */
    private int indexMask;
}
