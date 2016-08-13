package com.y3r9.c47.dog.swj.model.parallel.spi;

/**
 * The Interface ParallelBuilder.
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 * @see NameBuilder, ParallelParam
 * @since project 3.0
 */
public interface ParallelBuilder<D, C, R> extends NameBuilder, ParallelParam {

    /**
     * Gets the node builder. This builder is optional. In general cases, the default node builder
     * inside the parallel graph provider is enough.
     *
     * @return the node builder
     */
    NodeBuilder<D, C, R> getNodeBuilder();

    /**
     * Gets the handler builder. This builder is optional. If the default node builder inside the
     * parallel graph provider is used, then this handler is required to set for the handler.
     *
     * @return the handler builder
     */
    HandlerBuilder<D, C, R> getHandlerBuilder();

    /**
     * Gets builder name.
     *
     * @return the builder name
     * @since 1.1
     */
    String getBuilderName();

}
