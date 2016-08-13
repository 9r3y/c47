package com.y3r9.c47.dog.swj.model.parallel;

import java.io.IOException;

import cn.com.netis.dp.commons.common.ConfigHint;
import com.y3r9.c47.dog.swj.polling.CallbackIdleSleepThread;
import com.y3r9.c47.dog.swj.polling.spi.ThreadCallback;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class WorkNodeThreadProvider.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 * @see WorkNodeProvider, ThreadCallback
 * @since project 3.0
 */
public class WorkNodeThreadProvider<D, C, R> extends WorkNodeProvider<D, C, R> implements
        ThreadCallback {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(WorkNodeThreadProvider.class);

    @Override
    public boolean processItem() {
        if (getNodeName().startsWith("DATA-WORK")) {
            int a = 1;
        }
        /**
         * Can be extended.
         */
        return processWorkData();
    }

    @Override
    public boolean notifyAfterThreadOpen() {
        /**
         * Can be extended.
         */
        return getWorkHandler().notifyAfterThreadOpen();
    }

    @Override
    public boolean notifyBeforeThreadClose() {
        /**
         * Can be extended.
         */
        return getWorkHandler().notifyBeforeThreadClose();
    }

    @Override
    public void open() throws IOException {
        /**
         * Can be extended.
         */

        // 1st: open super
        super.open();

        // 2nd: start work thread
        thread.startThread();

        // 3rd: log started
        LOG.debug(new StringBuilder().append("WorkNodeThread=").append(thread.getThreadName())
                .append(" started its thread.").toString());
    }

    @Override
    public void close() throws IOException {
        /**
         * Can be extended.
         */

        // 1st: close super
        super.close();

        // 2nd: stop work thread
        try {
            thread.asynStop();
            thread.joinThread();
        } catch (Exception e) {
            LOG.debug(new StringBuilder().append("WorkNodeThread=").append(thread.getThreadName())
                    .append(" detects an exception  while to close its thread. exception=")
                    .append(e.toString()).toString());
        }

        // 3rd: log closed
        LOG.debug(new StringBuilder().append("WorkNodeThread=").append(thread.getThreadName())
                .append(" closed.").toString());
    }

    @Override
    public long getIdleCount() {
        return thread.getIdleStrategy().getIdleCount();
    }

    @Override
    public long getIdleTimeCost() {
        return thread.getIdleStrategy().getIdleTimeCost();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("WorkNodeThreadProvider [thread=").append(thread).append(", toString()=")
                .append(super.toString()).append("]");
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

    /**
     * Instantiates a new work node provider.
     * 
     * @param name the name
     */
    public WorkNodeThreadProvider(final String name) {
        super(name);
        thread = new CallbackIdleSleepThread(name);
        thread.finalSetCallback(this);
        thread.setIdleStrategy(DEFAULT_WORK_NODE_IDLE_PARK_MODE,
                DEFAULT_WORK_NODE_IDLE_RETRY_COUNT);
    }

    /**
     * Instantiates a new work node provider.
     * 
     * @param id the id
     * @param name the name
     */
    public WorkNodeThreadProvider(final int id, final String name) {
        this(name);
        thread.setThreadId(id);
    }

    /** The pipeline name. */
    private final CallbackIdleSleepThread thread;
}
