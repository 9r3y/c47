package com.y3r9.c47.dog.swj.model.parallel;

import com.y3r9.c47.dog.swj.model.parallel.spi.JoinHandler;
import com.y3r9.c47.dog.swj.model.parallel.spi.JoinNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionScheduler;
import com.y3r9.c47.dog.swj.model.parallel.spi.SplitWorkJoinNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.WorkHandler;
import com.y3r9.c47.dog.swj.model.parallel.spi.WorkNode;
import com.y3r9.c47.dog.swj.model.sort.spi.SortPipe;
import com.y3r9.c47.dog.swj.model.sort.spi.SortPipePut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The Class AbstractSplitWorkJoinNode.
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 * @see AbstractSplitNode, SplitWorkJoinNode, PartitionHandler, SortPipe
 * @since project 3.0
 */
abstract class AbstractSplitWorkJoinNode<D, C, R> extends AbstractSplitNode<D, C, R>
        implements SplitWorkJoinNode<D, C, R>, SortPipe<R> {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSplitWorkJoinNode.class);

    /** The Constant WAIT_INBAND_TERMINATE_TIMEOUT. */
    public static final int WAIT_INBAND_TERMINATE_TIMEOUT = 30;

    /**
     * Do dispatch data.
     *
     * @param token the token
     * @param data the data
     */
    protected final void doDispatchData(final long token, final D data) {
        // consume directly
        final PartitionNode<D, C, R> part = selectPartition(token, data);
        workHandler.consumeWorkJob(token, data, part.getContext());
        handleCount++;
    }

    @Override
    public void open() throws IOException {
        /**
         * Can be extended.
         */

        // 1st: open next pipeline
        joinHandler.open();
        LOG.debug(new StringBuilder().append("JoinNode=").append(getNodeName())
                .append(" open its handler=").append(joinHandler.getNodeName())
                .append(" successfully.").toString());

        // 2nd: log started
        LOG.debug(new StringBuilder().append("SplitWorkJoinNode=").append(getNodeName())
                .append(" started.").toString());
    }

    @Override
    public void close() throws IOException {
        /**
         * Can be extended.
         */

        // 1st: wait for handler to detect the terminate signal
        if (joinHandler.waitForInbandTerminate(WAIT_INBAND_TERMINATE_TIMEOUT)) {
            LOG.debug(new StringBuilder().append("JoinNode=").append(getNodeName())
                    .append(" waits the inband terminate from its handler=")
                    .append(joinHandler.getNodeName()).append(" successfully.")
                    .toString());
        } else {
            LOG.warn(new StringBuilder().append("JoinNode=").append(getNodeName())
                    .append(" waits the inband terminate from its handler=")
                    .append(joinHandler.getNodeName()).append(" failed, timeout=")
                    .append(WAIT_INBAND_TERMINATE_TIMEOUT).append("s.").toString());
        }

        // 2nd: close next pipeline
        try {
            joinHandler.close();
        } catch (Exception e) {
            LOG.debug(new StringBuilder().append("JoinNode=").append(getNodeName())
                    .append(" detects an exception  while to close its handler=")
                    .append(joinHandler.getNodeName()).append(" failed. exception=")
                    .append(e.toString()).toString());
        }

        // 3rd: log closed
        LOG.debug(new StringBuilder().append("SplitWorkJoinNode=").append(getNodeName())
                .append(" closed.").toString());
    }

    @Override
    public final List<WorkNode<D, C, R>> getWorkNodes() {
        final List<WorkNode<D, C, R>> result = new LinkedList<>();
        result.add(this);
        return result;
    }

    @Override
    public final void setWorkNodes(final List<WorkNode<D, C, R>> workNodes) {
        throw new UnsupportedOperationException(
                "AbstractSplitWorkJoinNode#setWorkNodes(List<WorkNode<D, C, R>>)");
    }

    @Override
    public final JoinNode<R> getJoinNode() {
        return this;
    }

    @Override
    public final void setJoinNode(final JoinNode<R> joinNode) {
        throw new UnsupportedOperationException("AbstractSplitWorkJoinNode#setJoinNode(JoinNode)");
    }

    @Override
    public final void setWorkHandler(final WorkHandler<D, C, R> value) {
        workHandler = value;
        updateWorkHandlerJoinPipe();
    }

    /**
     * Update work handler join pipe.
     */
    private void updateWorkHandlerJoinPipe() {
        if (workHandler != null) {
            workHandler.setJoinPipe(this);
        }
    }

    @Override
    public final WorkHandler<D, C, R> getWorkHandler() {
        return workHandler;
    }

    @Override
    public final void setPartScheduler(final PartitionScheduler<D, C, R> value) {
        throw new UnsupportedOperationException(
                "AbstractSplitWorkJoinNode#setPartHandler(PartitionHandler)");
    }

    @Override
    public final PartitionScheduler<D, C, R> getPartScheduler() {
        return getPartitionNodeManager();
    }

    @Override
    public final void setJoinPipePut(final SortPipePut<R> value) {
        // ignore this function
        updateWorkHandlerJoinPipe();
    }

    @Override
    public final SortPipePut<R> getJoinPipePut() {
        return this;
    }

    @Override
    public final void setJoinPipeCapability(final int value) {
        setCapability(value);
    }

    @Override
    public final int getJoinPipeCapability() {
        return 1;
    }

    @Override
    public final SortPipe<R> getJoinPipe() {
        return this;
    }

    @Override
    public final void setJoinPipe(final SortPipe<R> value) {
        throw new UnsupportedOperationException("AbstractSplitWorkJoinNode#setJoinPipe(SortPipe)");
    }

    @Override
    public final void setJoinHandler(final JoinHandler<R> value) {
        joinHandler = value;
    }

    @Override
    public final JoinHandler<R> getJoinHandler() {
        return joinHandler;
    }

    @Override
    public void put(final long token, final R value) {
        // consume directly
        joinHandler.consumeJoinResult(value);
        outToken.incrementAndGet();
    }

    @Override
    public final long getInTokenThre() {
        return getOutToken() + capability();
    }

    @Override
    public final int capability() {
        return 1;
    }

    @Override
    public final long getOutToken() {
        return outToken.get();
    }

    @Override
    public final void setCapability(final int value) {
        throw new UnsupportedOperationException("AbstractSplitWorkJoinNode#setCapability(int)");
    }

    @Override
    public final R pop() {
        throw new UnsupportedOperationException("AbstractSplitWorkJoinNode#pop()");
    }

    @Override
    public void pop(final List<R> result) {
        throw new UnsupportedOperationException("AbstractSplitWorkJoinNode#pop(List<R>)");
    }

    @Override
    public final long getTryCount() {
        return handleCount;
    }

    @Override
    public final long getSuccessCount() {
        return handleCount;
    }

    @Override
    public final long getFailCount() {
        return 0;
    }

    @Override
    public final long getEmptyCount() {
        return 0;
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
    public long getOutTokenPosition() {
        return getOutToken();
    }

    @Override
    public int getJoinNodeCapability() {
        return getJoinPipeCapability();
    }

    @Override
    public long getPopSuccessCount() {
        return handleCount;
    }

    @Override
    public long getPopFailCount() {
        return 0;
    }

    @Override
    public void setSelector(final int value) {
        throw new UnsupportedOperationException("AbstractSplitWorkJoinNode#setSelector(value)");
    }

    @Override
    public void reverseSelectorDirection() {
        throw new UnsupportedOperationException(
                "AbstractSplitWorkJoinNode#reverseSelectorDirection(value)");
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append(", outToken=").append(outToken)
                .append(", workHandler=").append(workHandler).append(", joinHandler=")
                .append(joinHandler);
        return builder.toString();
    }

    /**
     * Instantiates a new work node provider.
     *
     * @param name the name
     */
    public AbstractSplitWorkJoinNode(final String name) {
        super(name);
        setJoinPipeStatus(this);
    }

    /** The Handle count. */
    private long handleCount;

    /** The work handler. */
    private WorkHandler<D, C, R> workHandler;

    /** The join handler. */
    private JoinHandler<R> joinHandler;

    /** The pop token. */
    private final AtomicLong outToken;

    {
        handleCount = 0;
        outToken = new AtomicLong();
    }
}
