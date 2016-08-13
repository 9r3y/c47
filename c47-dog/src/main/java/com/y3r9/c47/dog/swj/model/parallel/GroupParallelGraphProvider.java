package com.y3r9.c47.dog.swj.model.parallel;

import org.apache.commons.configuration.Configuration;

import cn.com.netis.dp.commons.lang.NonPositiveArgumentException;
import com.y3r9.c47.dog.swj.model.parallel.spi.GroupParallelGraph;
import com.y3r9.c47.dog.swj.model.parallel.spi.ParallelParam;

/**
 * The Class GroupParallelGraphProvider.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @see ParallelGraphProvider, GroupParallelGraph
 * @since project 3.0
 */
public class GroupParallelGraphProvider<D, C, R> extends ParallelGraphProvider<D, C, R> implements
        GroupParallelGraph<D, C, R> {

    @Override
    public final int getUsePartitionCount() {
        return groupCount * waysInsideGroup;
    }

    @Override
    public final int getWaysInsideGroup() {
        return waysInsideGroup;
    }

    @Override
    public final int getGroupCount() {
        return groupCount;
    }

    @Override
    public final void setGroupCount(final int value) {
        NonPositiveArgumentException.check(value, "groupCount");
        groupCount = value;
    }

    /**
     * Sets the ways inside group.
     * 
     * @param value the new ways inside group
     */
    public final void setWaysInsideGroup(final int value) {
        NonPositiveArgumentException.check(value, "waysInsideGroup");
        waysInsideGroup = value;
    }

    /**
     * Update parameter.
     * 
     * @param parallelConfig the parallel configuration
     * @param parallelParam the default parameter
     */
    @Override
    protected void updateParameter(final Configuration parallelConfig,
            final ParallelParam parallelParam) {
        /**
         * Can be extended.
         */
        // create auto parameter
        final GroupParallelGraphAutoParam p = ParallelUtils.getGroupGraphAutoParameter(
                parallelConfig, parallelParam.getWorkerCount(), groupCount);
        setWaysInsideGroup(p.getWaysInsideGroup());

        // apply the parameter
        parallelParam.updateParam(p);
    }

    /** The ways inside group. */
    private int waysInsideGroup;

    /** The group count. */
    private int groupCount;

    {
        // to avoid zero
        groupCount = 1;
    }
}
