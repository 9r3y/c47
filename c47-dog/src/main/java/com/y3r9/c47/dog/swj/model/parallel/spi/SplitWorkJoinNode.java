package com.y3r9.c47.dog.swj.model.parallel.spi;

/**
 * The Interface SplitWorkJoinNode.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @see PipelineInput, PartitionHandler, Configurable
 * @since project 3.0
 */
public interface SplitWorkJoinNode<D, C, R> extends SplitNode<D, C, R>, WorkNode<D, C, R>,
        JoinNode<R> {
}
