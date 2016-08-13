package com.y3r9.c47.dog.swj.model.parallel;

import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionScheduler;
import com.y3r9.c47.dog.swj.model.parallel.spi.WorkHandler;
import com.y3r9.c47.dog.swj.model.parallel.spi.WorkNode;
import com.y3r9.c47.dog.swj.model.sort.spi.SortPipePut;

/**
 * The Class WorkNodeProvider.
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.4
 * @see WorkNode
 * @since project 3.0
 */
public class WorkNodeProvider<D, C, R> implements WorkNode<D, C, R> {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(WorkNodeProvider.class);

    /**
     * Process work data.
     * Only can be invoked by derived class.
     *
     * @return <code>true</code> if at least one item has been processed, <code>false</code> if
     * there is no item processed
     */
    protected final boolean processWorkData() {
        final PartitionNode<D, C, R> part = partScheduler.getPartition(selector,
                lastConsumedPartition);
        updateSelector();

        int count = 0;
        if (part != null) {
            /*
             * Because selector is driven randomly, so in partition padding case, e.g. the group
             * parallel graph, there are NULL context partitions which need to be skipped.
             */
            final long threToken = getResultPipeInTokenThreshold();
            if (partScheduler.needLockPartition()) {
                count = part.tryConsumePartition(selector, workHandler, threToken);
            } else {
                count = part.consumePartition(selector, workHandler, threToken);
            }
        }
        lastConsumedPartition = part;

        tryCount++;
        if (count > 0) {
            successCount++;
            return true;
        } else if (count == 0) {
            emptyCount++;
            return false;
        } else {
            failCount++;
            return false;
        }
    }

    @Override
    public final String getNodeName() {
        return nodeName;
    }

    @Override
    public void open() throws IOException {
        /**
         * Can be extended.
         */

        // 1st: log started
        LOG.debug(new StringBuilder().append("WorkNode=").append(getNodeName()).append(" started.")
                .toString());
    }

    @Override
    public void close() throws IOException {
        /**
         * Can be extended.
         */

        // 1st: log closed
        LOG.debug(new StringBuilder().append("WorkNode=").append(getNodeName()).append(" closed.")
                .toString());
    }

    /**
     * Update selector.
     */
    private void updateSelector() {
        selector += selectStep;
        if (selectStep > 0) {
            if (selector == Integer.MAX_VALUE) {
                selector = 0;
            }
        } else {
            if (selector == 0) {
                selector = Integer.MAX_VALUE;
            }
        }
    }

    /**
     * Update work handler join pipe.
     */
    private void updateWorkHandlerJoinPipe() {
        if (workHandler != null) {
            workHandler.setJoinPipe(joinPipe);
        }
    }

    @Override
    public final WorkHandler<D, C, R> getWorkHandler() {
        return workHandler;
    }

    @Override
    public final void setWorkHandler(final WorkHandler<D, C, R> value) {
        workHandler = value;
        updateWorkHandlerJoinPipe();
    }

    @Override
    public final void setPartScheduler(final PartitionScheduler<D, C, R> value) {
        partScheduler = value;
    }

    @Override
    public final PartitionScheduler<D, C, R> getPartScheduler() {
        return partScheduler;
    }

    @Override
    public final SortPipePut<R> getJoinPipePut() {
        return joinPipe;
    }

    @Override
    public final void setJoinPipePut(final SortPipePut<R> value) {
        joinPipe = value;
        updateWorkHandlerJoinPipe();
    }

    @Override
    public final void setSelector(final int value) {
        this.selector = value;
    }

    @Override
    public final void reverseSelectorDirection() {
        selectStep *= -1;
    }

    /**
     * Gets the result pipe in token threshold.
     *
     * @return the result pipe in token threshold
     */
    public final long getResultPipeInTokenThreshold() {
        return joinPipe.getInTokenThre();
    }

    @Override
    public long getTryCount() {
        return tryCount;
    }

    @Override
    public long getSuccessCount() {
        return successCount;
    }

    @Override
    public long getFailCount() {
        return failCount;
    }

    @Override
    public long getEmptyCount() {
        return emptyCount;
    }

    @Override
    public long getIdleCount() {
        /**
         * Can be extended.
         */
        return 0;
    }

    @Override
    public long getIdleTimeCost() {
        /**
         * Can be extended.
         */
        return 0;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("WorkNodeProvider [nodeName=").append(nodeName).append(", selector=")
                .append(selector).append(", workHandler=").append(workHandler)
                .append(", partitionScheduler=").append(partScheduler).append("]");
        return builder.toString();
    }

    @Override
    public void updateConfiguration(final Configuration config, final int hint) {
        /**
         * Can be extended.
         */
    }

    /**
     * Instantiates a new work node provider.
     *
     * @param name the name
     */
    public WorkNodeProvider(final String name) {
        nodeName = name;
    }

    /** The Try count. */
    private long tryCount;

    /** The Success count. */
    private long successCount;

    /** The Fail count. */
    private long failCount;

    /** The Empty count. */
    private long emptyCount;

    /** The node name. */
    private final String nodeName;

    /** The selector. */
    private int selector;

    /** The work handler. */
    private WorkHandler<D, C, R> workHandler;

    /** The Part scheduler. */
    private PartitionScheduler<D, C, R> partScheduler;

    /** The join pipe. DO NOT ADD to toString(). */
    private SortPipePut<R> joinPipe;

    /** The Select step. */
    private int selectStep = 1;

    /** The Last consume part. */
    private PartitionNode<D, C, R> lastConsumedPartition;

}
