package com.y3r9.c47.dog.swj.model.parallel;

import cn.com.netis.dp.commons.lang.NonPositiveArgumentException;

/**
 * The Class ParallelGraphAutoParameter.
 * 
 * @version 1.0
 * @see AbstractGraphAutoParameter, ParallelGraphAutoParam
 * @since project 3.0
 */
public class GroupParallelGraphAutoParameter extends AbstractGraphAutoParameter implements
        GroupParallelGraphAutoParam {

    @Override
    public final int getUsePartitionCount() {
        return groupCount * waysInsideGroup;
    }

    @Override
    public final void init(final int workerCount, final int aGroupCount) {
        groupCount = aGroupCount;
        setWorkerCount(workerCount);
    }

    @Override
    protected void notifyToChangePartitionCount() {
        /**
         * Can be extended.
         */
        if (getWorkerCount() > 0) {
            setPartitionCount(getWorkNodeCount() * groupCount * waysInsideGroup);
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
    public final int getGroupCount() {
        return groupCount;
    }

    @Override
    public final int getWaysInsideGroup() {
        return waysInsideGroup;
    }

    @Override
    public final void setWaysInsideGroup(final Integer value) {
        if (value != null) {
            NonPositiveArgumentException.check(value, "waysInsideGroup");
            waysInsideGroup = value;
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
     * @param groupCount the group count
     */
    public GroupParallelGraphAutoParameter(final int workerCount, final int groupCount) {
        init(workerCount, groupCount);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ParallelGraphAutoParameter [").append(super.toString())
                .append(", groupCount=").append(groupCount).append(", waysInsideGroup=")
                .append(waysInsideGroup).append(", partToPipeCapabilityFactor=")
                .append(partToPipeCapabilityFactor).append("]");
        return builder.toString();
    }

    /** The group count. */
    private int groupCount;

    /** The ways in group. */
    private int waysInsideGroup;

    /** The partition to pipe capability factor. */
    private int partToPipeCapabilityFactor;

    {
        setWaysInsideGroup(DEFAULT_WAYS_INSIDE_GROUP);
        setPartToPipeCapabilityFactor(DEFAULT_PART_TO_PIPE_CAPABILITY);
    }
}
