package com.y3r9.c47.dog.swj.model.parallel.spi;

/**
 * The Interface PartitionParam.
 * 
 * @version 1.0
 * @since project 3.0
 */
public interface PartitionParam {

    /**
     * Gets the worker count.
     * 
     * @return the worker count
     */
    int getWorkerCount();

    /**
     * Gets the partition count.
     * 
     * @return the partition count
     */
    int getPartitionCount();

    /**
     * Gets the use partition count.
     * 
     * @return the use partition count
     */
    int getUsePartitionCount();

    /**
     * Gets the join pipe capability.
     * 
     * @return the join pipe capability
     */
    int getJoinPipeCapability();
}
