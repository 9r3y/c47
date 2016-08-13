package com.y3r9.c47.dog.swj.model.parallel;

import java.util.concurrent.CountDownLatch;

import com.y3r9.c47.dog.swj.model.parallel.spi.HandlerBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.NodeBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.ParallelBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.WorkNode;

/**
 * The Class ConcurrentlyNodeBuilder.
 *
 * @param <D> the generic type
 * @param <C> the generic type
 * @param <R> the generic type
 * @version 1.0
 * @since project 3.2
 */
final class ConcurrentlyNodeBuilder<D, C, R> implements NodeFactory<D, C, R> {

    @SuppressWarnings("unchecked")
    @Override
    public WorkNode<D, C, R>[] createWorkNodes(final int count,
            final NodeBuilder<D, C, R> nodeBuilder, final ParallelBuilder<D, C, R> graphBuilder,
            final HandlerBuilder<D, C, R> handlerBuilder) {
        final WorkNode<D, C, R>[] workNodes = new WorkNode[count];
        final CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            new BuilderThread<>(i, workNodes, nodeBuilder, graphBuilder, handlerBuilder, latch)
                    .start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new IllegalArgumentException("more threads create workNode is error.");
        }
        return workNodes;
    }
}
