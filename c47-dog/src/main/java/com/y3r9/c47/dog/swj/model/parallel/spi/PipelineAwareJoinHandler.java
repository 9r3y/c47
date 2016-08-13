package com.y3r9.c47.dog.swj.model.parallel.spi;

import com.y3r9.c47.dog.swj.model.pipeline.PipelineOutput;

/**
 * The Interface PipelineAwareJoinHandler.
 * 
 * @param <R> the generic result data type
 * @param <NextData> the generic data type of next pipeline
 * @version 1.0
 * @see JoinHandler, PipelineOutput
 * @since project 3.0
 */
public interface PipelineAwareJoinHandler<R, NextData> extends JoinHandler<R>,
        PipelineOutput<NextData> {
}
