package com.y3r9.c47.dog.swj.model.pipeline;

/**
 * The Interface PipelineEnd.
 * 
 * @param <ResultData> the generic result data type
 * @version 1.0
 * @since project 3.0
 */
public interface PipelineEnd<ResultData> {

    /**
     * Gets the last value.
     * 
     * @return the last value
     */
    ResultData getLastValue();
}
