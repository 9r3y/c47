package com.y3r9.c47.dog.swj.model.parallel;

import com.y3r9.c47.dog.swj.model.parallel.spi.GraphAutoParam;

/**
 * The Interface GroupParallelGraphAutoParam.
 * 
 * @version 1.0
 * @since GraphAutoParam
 * @since project 3.0
 */
public interface GroupParallelGraphAutoParam extends GraphAutoParam {

    /** The Constant DEFAULT_WAYS_INSIDE_GROUP. */
    int DEFAULT_WAYS_INSIDE_GROUP = 1;

    /** The Constant DEFAULT_PART_TO_PIPE_CAPABILITY. */
    int DEFAULT_PART_TO_PIPE_CAPABILITY = 16;

    /**
     * Inits the auto parameter.
     * 
     * @param workerCount the worker count
     * @param groupCount the group count
     */
    void init(int workerCount, int groupCount);

    /**
     * Gets the group count.
     * 
     * @return the group count
     */
    int getGroupCount();

    /**
     * Gets the ways in group.
     * 
     * @return the ways in group
     */
    int getWaysInsideGroup();

    /**
     * Sets the ways inside group.
     * 
     * @param value the new ways inside group
     */
    void setWaysInsideGroup(Integer value);

    /**
     * Gets the part to pipe capability factor.
     * 
     * @return the part to pipe capability factor
     */
    int getPartToPipeCapabilityFactor();

    /**
     * Sets the part to pipe capability factor.
     * 
     * @param value the new part to pipe capability factor. <code>null</code> for the parameter is
     *            missing.
     */
    void setPartToPipeCapabilityFactor(Integer value);
}
