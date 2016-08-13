package com.y3r9.c47.dog.swj.model.parallel;

import java.io.IOException;

import cn.com.netis.dp.commons.common.ConfigHint;
import com.y3r9.c47.dog.swj.model.collection.SafeInPairDualQueue;
import com.y3r9.c47.dog.swj.model.collection.spi.PairDualQueue;
import com.y3r9.c47.dog.swj.polling.CallbackIdleSleepThread;
import com.y3r9.c47.dog.swj.polling.spi.ThreadCallback;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SplitWorkJoinNodeThreadProvider.
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 * @see AbstractSplitWorkJoinNode, ThreadCallback
 * @since project 3.0
 */
public class SplitWorkJoinNodeThreadProvider<D, C, R> extends AbstractSplitWorkJoinNode<D, C, R>
        implements ThreadCallback {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory
            .getLogger(SplitWorkJoinNodeThreadProvider.class);

    @Override
    protected final void processTokenData(final long token, final D data) {
        // thread safe, for the other thread to add data to the IN queue
        cacheQueue.addToInQueue(token, data);
    }

    @Override
    public final boolean processItem() {
        // thread safe, for this work thread to drain data from the IN queue to the OUT queue
        if (cacheQueue.isOutQueueEmpty()) {
            cacheQueue.safeDrainInQueueToOut();
        }

        int result = 0;

        // work till the threshold token
        while (!cacheQueue.isOutQueueEmpty()) {
            final long token = cacheQueue.peekOutQueueForHeader();
            final D data = cacheQueue.removeOutQueueForData();
            doDispatchData(token, data);
            result++;
        }

        return result > 0;
    }

    @Override
    public final boolean notifyAfterThreadOpen() {
        // no more data related to the thread provider
        return getWorkHandler().notifyAfterThreadOpen()
                && getJoinHandler().notifyAfterThreadOpen();
    }

    @Override
    public final boolean notifyBeforeThreadClose() {
        return getWorkHandler().notifyBeforeThreadClose()
                && getJoinHandler().notifyBeforeThreadClose();
    }

    @Override
    public void open() throws IOException {
        /**
         * Can be extended.
         */

        // 1st: open super
        super.open();

        // 2nd: start split thread
        thread.startThread();

        // 3rd: log started
        LOG.debug(new StringBuilder().append("SplitWorkJoinNodeThread=")
                .append(thread.getThreadName()).append(" started its thread.").toString());
    }

    @Override
    public void close() throws IOException {
        /**
         * Can be extended.
         */

        // 1st: close super
        super.close();

        // 2nd: stop split thread
        try {
            thread.asynStop();
            thread.joinThread();
        } catch (Exception e) {
            LOG.debug(new StringBuilder().append("SplitWorkJoinNodeThread=")
                    .append(thread.getThreadName())
                    .append(" detects an exception  while to close its thread. exception=")
                    .append(e.toString()).toString());
        }

        // 3rd: log closed
        LOG.debug(new StringBuilder().append("SplitWorkJoinNodeThread=")
                .append(thread.getThreadName()).append(" closed.").toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SplitWorkJoinNodeThreadProvider [").append(super.toString())
                .append(", thread=").append(thread).append("]");
        return builder.toString();
    }

    @Override
    public void updateConfiguration(final Configuration config, final int hint) {
        /**
         * Can be extended.
         */

        super.updateConfiguration(config, hint);

        if (config == null) {
            return;
        }
        if (ConfigHint.NATIVE_FILE == hint || ConfigHint.CLI_OVERRIDE == hint) {
            thread.updateConfiguration(config, hint);
        }
    }

    @Override
    public long getIdleCount() {
        return thread.getIdleStrategy().getIdleCount();
    }

    @Override
    public long getIdleTimeCost() {
        return thread.getIdleStrategy().getIdleTimeCost();
    }

    /**
     * Instantiates a new split work join node thread provider.
     *
     * @param name the name
     */
    public SplitWorkJoinNodeThreadProvider(final String name) {
        super(name);
        thread = new CallbackIdleSleepThread(name);
        thread.finalSetCallback(this);
        thread.setIdleStrategy(DEFAULT_WORK_NODE_IDLE_PARK_MODE,
                DEFAULT_WORK_NODE_IDLE_RETRY_COUNT);
    }

    /** The pipeline name. */
    private final CallbackIdleSleepThread thread;

    /** The cache queue. */
    private final transient PairDualQueue<Long, D> cacheQueue;

    {
        // it should always use safe IN dual queue.
        cacheQueue = new SafeInPairDualQueue<>();
    }
}
