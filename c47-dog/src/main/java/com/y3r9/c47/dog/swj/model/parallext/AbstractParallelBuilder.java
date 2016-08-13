package com.y3r9.c47.dog.swj.model.parallext;

import com.y3r9.c47.dog.swj.model.parallel.ParallelParameter;
import com.y3r9.c47.dog.swj.model.parallel.spi.*;

/**
 * The Class AbstractParallelBuilder.
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @see ParallelParameter, ParallelBuilder, HandlerBuilder
 * @since project 3.0
 */
public abstract class AbstractParallelBuilder<D, C, R> extends ParallelParameter implements
        ParallelBuilder<D, C, R>, HandlerBuilder<D, C, R> {

    @Override
    public final String getJoinNodeName() {
        return String.format("%s-JOIN", builderName);
    }

    @Override
    public final String getSplitNodeName() {
        return String.format("%s-SPLIT", builderName);
    }

    @Override
    public final String getPartitionNodeTitle(final int partId) {
        return String.format("%s-PART-%d", builderName, partId);
    }

    @Override
    public final String getWorkNodeName(final int workerId) {
        return String.format("%s-WORK-%d", builderName, workerId);
    }

    @Override
    public final String getWorkHandlerName(final int workId) {
        return String.format("%s-WORK-HANDLER-%d", builderName, workId);
    }

    @Override
    public final NodeBuilder<D, C, R> getNodeBuilder() {
        // use default node builder, so no new node builder required
        return null;
    }

    @Override
    public final HandlerBuilder<D, C, R> getHandlerBuilder() {
        return this;
    }

    @Override
    public final JoinHandler<R> getJoinHandler() {
        return joinHandler;
    }

    @Override
    public final DispatchSpanHandler<D> getSpanHandler() {
        return spanHandler;
    }

    /**
     * Sets the join handler.
     *
     * @param value the new join handler
     */
    public final void setJoinHandler(final JoinHandler<R> value) {
        joinHandler = value;
    }

    /**
     * Sets the span handler.
     *
     * @param value the new span handler
     */
    public final void setSpanHandler(final DispatchSpanHandler<D> value) {
        spanHandler = value;
    }

    /**
     * Gets the builder name.
     *
     * @return the builder name
     */
    @Override
    public final String getBuilderName() {
        return builderName;
    }

    /**
     * Instantiates a new abstract parallel builder.
     *
     * @param name the name
     */
    protected AbstractParallelBuilder(final String name) {
        super();
        builderName = name;
    }

    /** The builder name. */
    private final String builderName;

    /** The join handler. */
    private JoinHandler<R> joinHandler;

    /** The span handler. */
    private DispatchSpanHandler<D> spanHandler;
}
