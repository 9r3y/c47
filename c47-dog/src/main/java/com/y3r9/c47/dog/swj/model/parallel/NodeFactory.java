package com.y3r9.c47.dog.swj.model.parallel;

import com.y3r9.c47.dog.swj.model.parallel.spi.HandlerBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.NodeBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.ParallelBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.WorkNode;

/**
 * The Interface NodeFactory.
 * 
 * @version 1.0
 * @since project 3.2
 * 
 * @param <D> the generic type
 * @param <C> the generic type
 * @param <R> the generic type
 */
public interface NodeFactory<D, C, R> {

    /**
     * Creates the work nodes.
     *
     * @param count the count
     * @param nodeBuilder the node builder
     * @param graphBuilder the graph builder
     * @param handlerBuilder the handler builder
     * @return the work node< d, c, r>[]
     */
    WorkNode<D, C, R>[] createWorkNodes(int count, NodeBuilder<D, C, R> nodeBuilder,
            ParallelBuilder<D, C, R> graphBuilder, HandlerBuilder<D, C, R> handlerBuilder);
}
