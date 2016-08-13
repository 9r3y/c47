package com.y3r9.c47.dog.swj.model.parallel;

import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.netis.dp.commons.common.ConfigHint;
import com.y3r9.c47.dog.swj.polling.CallbackIdleSleepThread;
import com.y3r9.c47.dog.swj.polling.spi.ThreadCallback;

/**
 * The Class AbstractJoinNodeThreadProvider.
 * 
 * @param <R> the generic result data type
 * @version 1.0
 * @see AbstractJoinNodeProvider, ThreadCallback
 * @since project 3.0
 */
public abstract class AbstractJoinNodeThreadProvider<R> extends AbstractJoinNodeProvider<R>
        implements ThreadCallback {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractJoinNodeThreadProvider.class);

    /** The Constant WAIT_INBAND_TERMINATE_TIMEOUT. */
    public static final int WAIT_INBAND_TERMINATE_TIMEOUT = 30;

    @Override
    public final boolean notifyAfterThreadOpen() {
        /**
         * Can be extended.
         */
        return getJoinHandler().notifyAfterThreadOpen();
    }

    @Override
    public final boolean notifyBeforeThreadClose() {
        /**
         * Can be extended.
         */
        return getJoinHandler().notifyBeforeThreadClose();
    }

    @Override
    public final void open() throws IOException {
        // 1st: open super
        super.open();

        // 2nd: start join thread
        thread.startThread();

        // log started
        LOG.debug(new StringBuilder().append("JoinNodeThread=").append(thread.getThreadName())
                .append(" started its thread.").toString());
    }

    @Override
    public final void close() throws IOException {
        // 1st: close super
        super.close();

        // 2nd: stop join thread
        try {
            thread.asynStop();
            thread.joinThread();
        } catch (Exception e) {
            LOG.debug(new StringBuilder().append("JoinNodeThread=").append(thread.getThreadName())
                    .append(" detects an exception while to stop its thread. exception=")
                    .append(e.toString()).toString());
        }

        // 3rd: log closed
        LOG.debug(new StringBuilder().append("JoinNodeThread=").append(thread.getThreadName())
                .append(" closed.").toString());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append(", thread=").append(thread);
        return builder.toString();
    }

    @Override
    public final void updateConfiguration(final Configuration config, final int hint) {
        if (config == null) {
            return;
        }
        if (ConfigHint.NATIVE_FILE == hint || ConfigHint.CLI_OVERRIDE == hint) {
            thread.updateConfiguration(config, hint);
        }
    }

    /**
     * Instantiates a new abstract thread node.
     * 
     * @param name the name
     */
    public AbstractJoinNodeThreadProvider(final String name) {
        super(name);
        thread = new CallbackIdleSleepThread(name);
        thread.finalSetCallback(this);
        thread.setIdleStrategy(DEFAULT_JOIN_NODE_IDLE_PARK_MODE,
                DEFAULT_JOIN_NODE_IDLE_RETRY_COUNT);
    }

    /** The pipeline name. */
    private final CallbackIdleSleepThread thread;
}
