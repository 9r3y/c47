package com.y3r9.c47.dog.swj.model.parallel.spi;

/**
 * The Interface ParallelParam.
 * 
 * @version 1.0
 * @see PartitionParam
 * @since project 3.0
 */
public interface ParallelParam extends PartitionParam {

    /** The default worker count. */
    int DEFAULT_WORKER_COUNT = 8;

    /**
     * Update parameter.
     * 
     * @param autoParam the auto parameter
     */
    void updateParam(GraphAutoParam autoParam);
}
