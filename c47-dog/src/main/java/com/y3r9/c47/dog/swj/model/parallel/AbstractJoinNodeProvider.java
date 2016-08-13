package com.y3r9.c47.dog.swj.model.parallel;

import com.y3r9.c47.dog.swj.model.parallel.spi.JoinHandler;
import com.y3r9.c47.dog.swj.model.parallel.spi.JoinNode;
import com.y3r9.c47.dog.swj.model.sort.NativeSortPipeProvider;
import com.y3r9.c47.dog.swj.model.sort.spi.SortPipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * The Class AbstractJoinNodeProvider.
 *
 * @param <R> the generic result data type
 * @version 1.0
 * @see JoinNode
 * @since project 3.0
 */
public abstract class AbstractJoinNodeProvider<R> implements JoinNode<R> {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractJoinNodeProvider.class);

    /** The Constant WAIT_INBAND_TERMINATE_TIMEOUT. */
    public static final int WAIT_INBAND_TERMINATE_TIMEOUT = 30;

    /**
     * Process batch data.
     *
     * @return true, if successful
     */
    protected final boolean processBatchData() {
        data.clear();
        joinPipe.pop(data);
        for (R item : data) {
            joinHandler.consumeJoinResult(item);
        }
        if (data.size() > 0) {
            popSuccessCount++;
            return true;
        } else {
            popFailCount++;
            return false;
        }
    }

    /**
     * Process single data.
     *
     * @return true, if successful
     */
    protected final boolean processSingleData() {
        final R item = joinPipe.pop();
        joinHandler.consumeJoinResult(item);
        if (item != null) {
            popSuccessCount++;
            return true;
        } else {
            popFailCount++;
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
         * Will be extended.
         */

        // 1st: open next pipeline
        joinHandler.open();
        LOG.debug(new StringBuilder().append("JoinNode=").append(getNodeName())
                .append(" open its handler=").append(joinHandler.getNodeName())
                .append(" successfully.").toString());

        // 2nd, log started
        LOG.debug(new StringBuilder().append("JoinNode=").append(getNodeName()).append(" started.")
                .toString());
    }

    @Override
    public void close() throws IOException {
        /**
         * Will be extended.
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
        LOG.debug(new StringBuilder().append("JoinNode=").append(getNodeName())
                .append(" closed.").toString());
    }

    @Override
    public final void setJoinPipeCapability(final int value) {
        joinPipe.setCapability(value);
    }

    @Override
    public final int getJoinPipeCapability() {
        return joinPipe.capability();
    }

    @Override
    public final SortPipe<R> getJoinPipe() {
        return joinPipe;
    }

    @Override
    public final void setJoinPipe(final SortPipe<R> value) {
        joinPipe = value;
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
    public long getOutTokenPosition() {
        return joinPipe.getOutToken();
    }

    @Override
    public int getJoinNodeCapability() {
        return joinPipe.capability();
    }

    @Override
    public long getPopSuccessCount() {
        return popSuccessCount;
    }

    @Override
    public long getPopFailCount() {
        return popFailCount;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("nodeName=").append(nodeName).append(", joinHandler=").append(joinHandler);
        return builder.toString();
    }

    /**
     * Instantiates a new abstract thread node.
     *
     * @param name the name
     */
    public AbstractJoinNodeProvider(final String name) {
        nodeName = name;
    }

    /** The node name. */
    private final String nodeName;

    /** The join pipe. DO NOT ADD to toString(). */
    private transient SortPipe<R> joinPipe;

    /** The join handler. */
    private JoinHandler<R> joinHandler;

    /** The data. */
    private final transient List<R> data;

    /** The Pop success count. */
    private long popSuccessCount;

    /** The Pop fail count. */
    private long popFailCount;

    {
        joinPipe = new NativeSortPipeProvider<>();
        data = new LinkedList<>();
    }
}
