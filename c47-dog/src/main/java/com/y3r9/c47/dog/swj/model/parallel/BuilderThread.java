package com.y3r9.c47.dog.swj.model.parallel;

import java.util.concurrent.CountDownLatch;

import com.y3r9.c47.dog.swj.model.parallel.spi.HandlerBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.NodeBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.ParallelBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.WorkNode;

/**
 * The Class BuilderThread.
 *
 * @param <D> the generic type
 * @param <C> the generic type
 * @param <R> the generic type
 * @version 1.0
 * @since project 3.2
 */
final class BuilderThread<D, C, R> extends Thread {

    @Override
    public void run() {
        try {
            final WorkNode<D, C, R> workNode = nodeBuilder.createWorkNode(index,
                    graphBuilder.getWorkNodeName(index));
            if (workNode == null) {
                throw new IllegalArgumentException("workNode is EMPTY.");
            }
            if (workNode.getWorkHandler() == null) {
                if (handlerBuilder != null) {
                    workNode.setWorkHandler(handlerBuilder.getWorkHandler(index));
                }
                if (workNode.getWorkHandler() == null) {
                    throw new IllegalArgumentException("getWorkHandler() of workNode is EMPTY.");
                }
            }
            // put into work nodes
            workNodes[index] = workNode;
        } finally {
            // always count down to avoid blocking
            latch.countDown();
        }
    }

    /**
     * The Constructor.
     *
     * @param indexVal the index val
     * @param workNodesVal the work nodes val
     * @param nodeBuilderVal the node builder val
     * @param graphBuilderVal the graph builder val
     * @param handlerBuilderVal the handler builder val
     * @param latchVal the latch val
     */
    public BuilderThread(final int indexVal, final WorkNode<D, C, R>[] workNodesVal,
            final NodeBuilder<D, C, R> nodeBuilderVal,
            final ParallelBuilder<D, C, R> graphBuilderVal,
            final HandlerBuilder<D, C, R> handlerBuilderVal, final CountDownLatch latchVal) {
        index = indexVal;
        workNodes = workNodesVal;
        nodeBuilder = nodeBuilderVal;
        graphBuilder = graphBuilderVal;
        handlerBuilder = handlerBuilderVal;
        latch = latchVal;
    }

    /** The index. */
    private transient int index;

    /** The work nodes. */
    private transient WorkNode<D, C, R>[] workNodes;

    /** The graph builder. */
    private transient ParallelBuilder<D, C, R> graphBuilder;

    /** The node builder. */
    private transient NodeBuilder<D, C, R> nodeBuilder;

    /** The handler builder. */
    private transient HandlerBuilder<D, C, R> handlerBuilder;

    /** The latch. */
    private transient CountDownLatch latch;
}
