package com.y3r9.c47.dog.swj.model.parallext;

import cn.com.netis.dp.commons.common.statis.CacheObservable;
import com.y3r9.c47.dog.swj.model.pipeline.PipelineInput;
import com.y3r9.c47.dog.swj.model.pipeline.PipelineOutput;

/**
 * The Interface PipelinePair.
 * 
 * @param <Data> the generic data type
 * @param <NextData> the generic data type for next pipeline
 * 
 * @version 1.0
 * @see CacheObservable
 * @since project 3.0
 */
public interface PipelinePair<Data, NextData> extends CacheObservable {

    /**
     * Gets the left.
     * 
     * @return the left
     */
    PipelineInput<Data> getLeft();

    /**
     * Gets the right.
     * 
     * @return the right
     */
    PipelineOutput<NextData> getRight();
}
