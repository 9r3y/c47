package com.y3r9.c47.dog.swj.model.parallel;

import com.y3r9.c47.dog.swj.model.parallel.spi.GraphAutoParam;
import com.y3r9.c47.dog.swj.model.parallel.spi.ParallelParam;

/**
 * The Class ParallelParameter.
 * 
 * @version 1.0
 * @see ParallelParam
 * @since project 3.0
 */
public class ParallelParameter implements ParallelParam {

    @Override
    public final int getWorkerCount() {
        return workerCount;
    }

    @Override
    public final int getPartitionCount() {
        return partitionCount;
    }

    @Override
    public final int getUsePartitionCount() {
        return usePartitionCount;
    }

    @Override
    public final int getJoinPipeCapability() {
        return joinPipeCapability;
    }

    @Override
    public final void updateParam(final GraphAutoParam autoParam) {
        workerCount = autoParam.getWorkerCount();
        partitionCount = autoParam.getPartitionCount();
        usePartitionCount = autoParam.getUsePartitionCount();
        joinPipeCapability = autoParam.getJoinPipeCapability();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("workerCount=").append(workerCount).append(", partitionCount=")
                .append(partitionCount).append(", usePartitionCount=").append(usePartitionCount)
                .append(", joinPipeCapability=").append(joinPipeCapability);
        return builder.toString();
    }

    /**
     * Instantiates a new default parameter provider.
     * 
     * @param autoParam the auto parameter
     */
    public ParallelParameter(final GraphAutoParam autoParam) {
        updateParam(autoParam);
    }

    /**
     * Instantiates a new default parameter.
     * 
     * @param count the worker count
     */
    public ParallelParameter(final int count) {
        this(new ParallelGraphAutoParameter(count));
    }

    /**
     * Instantiates a new default partition parameter provider.
     */
    public ParallelParameter() {
        this(DEFAULT_WORKER_COUNT);
    }

    /** The worker count. */
    private int workerCount;

    /** The partition count. */
    private int partitionCount;

    /** The use partition count. */
    private int usePartitionCount;

    /** The join pipe capability. */
    private int joinPipeCapability;
}
