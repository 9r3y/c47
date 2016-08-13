package com.y3r9.c47.dog.swj.model.parallel;

import cn.com.netis.dp.commons.lang.NonPositiveArgumentException;

/**
 * The Class ParallelGraphAutoParameter.
 * 
 * @version 1.0
 * @see AbstractGraphAutoParameter, ParallelGraphAutoParam
 * @since project 3.0
 */
public class ParallelGraphAutoParameter extends AbstractGraphAutoParameter implements
        ParallelGraphAutoParam {

    @Override
    public final int getUsePartitionCount() {
        return getPartitionCount();
    }

    @Override
    public final void init(final int workerCount) {
        setWorkerCount(workerCount);
    }

    @Override
    protected void notifyToChangePartitionCount() {
        /**
         * Can be extended.
         */
        if (getWorkerCount() > 0) {
            setPartitionCount(getWorkNodeCount() * workerToPartFactor);
        }
    }

    @Override
    protected void notifyToChangePipeCapability() {
        /**
         * Can be extended.
         */
        if (getPartitionCount() > 0) {
            setJoinPipeCapability(getPartitionCount() * partToPipeCapabilityFactor);
        }
    }

    @Override
    public final int getWorkerToPartFactor() {
        return workerToPartFactor;
    }

    @Override
    public final void setWorkerToPartFactor(final Integer value) {
        if (value != null) {
            NonPositiveArgumentException.check(value, "workerToPartFactor");
            workerToPartFactor = value;
            notifyToChangePartitionCount();
        }
    }

    @Override
    public final int getPartToPipeCapabilityFactor() {
        return partToPipeCapabilityFactor;
    }

    @Override
    public final void setPartToPipeCapabilityFactor(final Integer value) {
        if (value != null) {
            NonPositiveArgumentException.check(value, "partToPipeCapabilityFactor");
            partToPipeCapabilityFactor = value;
            notifyToChangePipeCapability();
        }
    }

    /**
     * Instantiates a new parallel graph auto parameter.
     * 
     * @param workerCount the worker count
     */
    public ParallelGraphAutoParameter(final int workerCount) {
        init(workerCount);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ParallelGraphAutoParameter [").append(super.toString())
                .append(", workerToPartFactor=").append(workerToPartFactor)
                .append(", partToPipeCapabilityFactor=").append(partToPipeCapabilityFactor)
                .append("]");
        return builder.toString();
    }

    /** The worker to partition factor. */
    private int workerToPartFactor;

    /** The partition to pipe capability factor. */
    private int partToPipeCapabilityFactor;

    {
        setWorkerToPartFactor(DEFAULT_WORKER_TO_PART_FACTOR);
        setPartToPipeCapabilityFactor(DEFAULT_PART_TO_PIPE_CAPABILITY);
    }
}
