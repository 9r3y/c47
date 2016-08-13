package com.y3r9.c47.dog.swj.model.pipeline;

import cn.com.netis.dp.commons.common.Node;

/**
 * The Interface PipelineInput. This pipeline is attempt for parallel usage.
 * 
 * @param <Data> the generic data type
 * @version 1.0
 * @since project 3.0
 */
public interface PipelineInput<Data> extends Node {

    /**
     * Dispatch the data into pipeline.
     * 
     * @param data the data
     */
    void dispatch(Data data);

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
}
