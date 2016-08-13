package com.y3r9.c47.dog.swj.model.pipeline;

/**
 * The Interface Pipeline.
 * 
 * @param <Data> the generic data type
 * @param <NextData> the generic data type for next pipeline
 * 
 * @version 1.0
 * @see PipelineInput, PipelineOutput
 * @since project 3.0
 */
public interface Pipeline<Data, NextData> extends PipelineInput<Data>, PipelineOutput<NextData> {

}
