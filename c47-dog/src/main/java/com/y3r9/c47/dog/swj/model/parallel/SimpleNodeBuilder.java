package com.y3r9.c47.dog.swj.model.parallel;

import com.y3r9.c47.dog.swj.model.parallel.spi.HandlerBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.NodeBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.ParallelBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.WorkNode;

/**
 * The Class SimpleNodeBuilder.
 *
 * @version 1.0
 * @since project 3.2
 * 
 * @param <D> the generic type
 * @param <C> the generic type
 * @param <R> the generic type
 */
final class SimpleNodeBuilder<D, C, R> implements NodeFactory<D, C, R> {

    @SuppressWarnings("unchecked")
    @Override
    public WorkNode<D, C, R>[] createWorkNodes(final int count,
            final NodeBuilder<D, C, R> nodeBuilder,
            final ParallelBuilder<D, C, R> graphBuilder,
            final HandlerBuilder<D, C, R> handlerBuilder) {
        final WorkNode<D, C, R>[] workNodes = new WorkNode[count];
        for (int i = 0; i < count; i++) {
            final WorkNode<D, C, R> workNode = nodeBuilder.createWorkNode(i,
                    graphBuilder.getWorkNodeName(i));
            if (workNode == null) {
                throw new IllegalArgumentException("workNode is EMPTY.");
            }
            if (workNode.getWorkHandler() == null) {
                if (handlerBuilder != null) {
                    workNode.setWorkHandler(handlerBuilder.getWorkHandler(i));
                }
                if (workNode.getWorkHandler() == null) {
                    throw new IllegalArgumentException("getWorkHandler() of workNode is EMPTY.");
                }
            }
            // put into work nodes
            workNodes[i] = workNode;
        }
        return workNodes;
    }

}
