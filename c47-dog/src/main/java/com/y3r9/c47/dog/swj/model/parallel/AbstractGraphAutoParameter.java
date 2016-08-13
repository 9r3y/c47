package com.y3r9.c47.dog.swj.model.parallel;

import cn.com.netis.dp.commons.lang.Constants;
import cn.com.netis.dp.commons.lang.NonPositiveArgumentException;
import com.y3r9.c47.dog.swj.value.Power2Utils;
import com.y3r9.c47.dog.swj.model.parallel.spi.GraphAutoParam;

/**
 * The Class AbstractGraphAutoParameter.
 * 
 * @version 1.0
 * @see GraphAutoParam
 * @since project 3.0
 */
public abstract class AbstractGraphAutoParameter implements GraphAutoParam {

    /**
     * Sets the worker count.
     * 
     * @param value the new worker count
     */
    protected final void setWorkerCount(final int value) {
        NonPositiveArgumentException.check(value, "workerCount");
        workerCount = value;
        notifyToChangePartitionCount();
    }

    /**
     * Notify to change partition count.
     */
    protected abstract void notifyToChangePartitionCount();

    /**
     * Notify to change pipe capability.
     */
    protected abstract void notifyToChangePipeCapability();

    @Override
    public final int getWorkerCount() {
        return workerCount;
    }

    /**
     * Gets the work node count.
     * 
     * @return the work node count
     */
    public final int getWorkNodeCount() {
        return ParallelUtils.getWorkNodeCount(workerCount);
    }

    @Override
    public final int getPartitionCount() {
        return partitionCount;
    }

    @Override
    public final int getJoinPipeCapability() {
        return joinPipeCapability;
    }

    @Override
    public final void setPartitionCount(final Integer value) {
        if (value != null) {
            NonPositiveArgumentException.check(value, "partitionCount");
            partitionCount = Power2Utils.power2Ceil(value, Constants.MIN_PARTITION_COUNT,
                    Constants.MAX_PARTITION_COUNT);
            notifyToChangePipeCapability();
        }
    }

    @Override
    public final void setJoinPipeCapability(final Integer value) {
        if (value != null) {
            NonPositiveArgumentException.check(value, "joinPipeCapability");
            joinPipeCapability = Power2Utils.power2Ceil(value, Constants.MIN_PIPE_CAPABILITY,
                    Constants.MAX_PIPE_CAPABILITY);
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("workerCount=").append(workerCount).append(", partitionCount=")
                .append(partitionCount).append(", joinPipeCapability=").append(joinPipeCapability);
        return builder.toString();
    }

    /** The worker count. */
    private int workerCount;

    /** The partition count. */
    private int partitionCount;

    /** The join pipe capability. */
    private int joinPipeCapability;
}
