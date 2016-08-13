package com.y3r9.c47.dog.swj.model.parallel;

import cn.com.netis.dp.commons.common.Threadable;
import com.y3r9.c47.dog.swj.model.parallel.spi.JoinHandler;
import com.y3r9.c47.dog.swj.model.parallel.spi.JoinNode;
import com.y3r9.c47.dog.swj.model.sort.AtomicSortPipeProvider;
import com.y3r9.c47.dog.swj.model.sort.spi.SortPipe;
import org.apache.commons.configuration.Configuration;
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
 * @see Thread, JoinNode,Threadable
 * @since project 3.0
 */
public final class JoinNodeThread2Provider<R> extends Thread implements JoinNode<R>, Threadable {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(JoinNodeThread2Provider.class);

    /** The Constant WAIT_INBAND_TERMINATE_TIMEOUT. */
    public static final int WAIT_INBAND_TERMINATE_TIMEOUT = 30;

    @Override
    public void run() {
        /**
         * Must open the I/O.
         */
        if (!notifyAfterThreadOpen()) {
            return;
        }

        try {
            while (!interrupted()) {
                if (!processBatchData()) {
                    break;
                }
            }
        } finally {
            /**
             * Must close the I/O which is opened.
             */
            notifyBeforeThreadClose();
        }
    }

    /**
     * Process batch data.
     *
     * @return true, if successful
     */
    boolean processBatchData() {
        data.clear();
        joinPipe.pop(data);
        for (R item : data) {
            joinHandler.consumeJoinResult(item);
        }
        if (data.size() > 0) {
            popSuccCount++;
        } else {
            popFailCount++;
        }
        return !terminate;
    }

    /**
     * Process single data.
     *
     * @return true, if successful
     */
    boolean processSingleData() {
        final R item = joinPipe.pop();
        joinHandler.consumeJoinResult(item);
        if (item != null) {
            popSuccCount++;
        } else {
            popFailCount++;
        }
        return !terminate;
    }

    @Override
    public int getThreadId() {
        return 0;
    }

    @Override
    public String getThreadName() {
        return getName();
    }

    @Override
    public void startThread() {
        terminate = false;
        start();
    }

    @Override
    public void joinThread() throws InterruptedException {
        join();
    }

    @Override
    public void asynStop() {
        terminate = true;
    }

    /**
     * Notify after thread open.
     *
     * @return true, if successful
     */
    public boolean notifyAfterThreadOpen() {
        /**
         * Can be extended.
         */
        return joinHandler.notifyAfterThreadOpen();
    }

    /**
     * Notify before thread close.
     *
     * @return true, if successful
     */
    public boolean notifyBeforeThreadClose() {
        /**
         * Can be extended.
         */
        return joinHandler.notifyBeforeThreadClose();
    }

    @Override
    public String getNodeName() {
        return nodeName;
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

        // 2nd, log started
        LOG.debug(new StringBuilder().append("JoinNode=").append(getNodeName()).append(" started.")
                .toString());

        // 3rd: start join thread
        startThread();

        // log started
        LOG.debug(new StringBuilder().append("JoinNodeThread=").append(getThreadName())
                .append(" started its thread.").toString());
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
        LOG.debug(new StringBuilder().append("JoinNode=").append(getNodeName())
                .append(" closed.").toString());

        // 4th: stop join thread
        try {
            asynStop();
            joinThread();
        } catch (Exception e) {
            LOG.debug(new StringBuilder().append("JoinNodeThread=").append(getThreadName())
                    .append(" detects an exception while to stop its thread. exception=")
                    .append(e.toString()).toString());
        }

        // 5th: log closed
        LOG.debug(new StringBuilder().append("JoinNodeThread=").append(getThreadName())
                .append(" closed.").toString());
    }

    @Override
    public void setJoinPipeCapability(final int value) {
        joinPipe.setCapability(value);
    }

    @Override
    public int getJoinPipeCapability() {
        return joinPipe.capability();
    }

    @Override
    public SortPipe<R> getJoinPipe() {
        return joinPipe;
    }

    @Override
    public void setJoinPipe(final SortPipe<R> value) {
        joinPipe = value;
    }

    @Override
    public void setJoinHandler(final JoinHandler<R> value) {
        joinHandler = value;
    }

    @Override
    public JoinHandler<R> getJoinHandler() {
        return joinHandler;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("AbstractJoinNodeProvider [").append(super.toString())
                .append(", joinHandler=")
                .append(joinHandler).append("]");
        return builder.toString();
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
        return popSuccCount;
    }

    @Override
    public long getPopFailCount() {
        return popFailCount;
    }

    @Override
    public void updateConfiguration(final Configuration config, final int hint) {
        /**
         * Can be extended.
         */
    }

    /**
     * Instantiates a new abstract thread node.
     *
     * @param name the name
     */
    public JoinNodeThread2Provider(final String name) {
        nodeName = name;
        setName(name);
    }

    /** The node name. */
    private final String nodeName;

    /** The terminate. */
    private boolean terminate;

    /** The join pipe. DO NOT ADD to toString(). */
    private transient SortPipe<R> joinPipe;

    /** The join handler. */
    private JoinHandler<R> joinHandler;

    /** The data. */
    private final transient List<R> data;

    /** The Pop fail count. */
    private transient long popFailCount;

    /** The Pop succ count. */
    private transient long popSuccCount;

    {
        joinPipe = new AtomicSortPipeProvider<>();
        data = new LinkedList<>();
    }
}
