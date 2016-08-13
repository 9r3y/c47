package com.y3r9.c47.dog.swj.model.pipeline;

import cn.com.netis.dp.commons.common.Node;

/**
 * The Interface PipelineOutput.
 * 
 * @param <NextData> the generic data type for next pipeline
 * @version 1.0
 * @since project 3.0
 */
public interface PipelineOutput<NextData> extends Node {

    /**
     * Gets the next pipeline.
     * 
     * @return the next pipeline
     */
    PipelineInput<NextData> getNextPipeline();

    /**
     * Sets the next pipeline.
     * 
     * @param value the new next pipeline
     */
    void setNextPipeline(PipelineInput<NextData> value);
}
