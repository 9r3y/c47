package com.y3r9.c47.dog.swj.polling;

import org.apache.commons.configuration.Configuration;

import cn.com.netis.dp.commons.common.ConfigHint;
import cn.com.netis.dp.commons.common.Threadable;
import cn.com.netis.dp.commons.lang.NotInitializedException;
import com.y3r9.c47.dog.swj.polling.spi.IdleStrategy;
import com.y3r9.c47.dog.swj.polling.spi.ParkMode;
import com.y3r9.c47.dog.swj.polling.spi.ThreadCallback;

/**
 * The Class CallbackIdleSleepThread.
 * 
 * <pre>
 * XML example for configuration:
 * 
 * <...>
 *   <idleParkMode>sleep</idleParkMode>
 *   <idleRetryCount>8</idleRetryCount>
 *   <ideaSleepNano>1</ideaSleepNano>
 * </...>
 * 
 * </pre>
 * 
 * @version 1.0
 * @see Thread, Threadable
 * @since project 3.0
 */
public class CallbackIdleSleepThread extends Thread implements Threadable {

    @Override
    public final void run() {
        if (callback == null) {
            throw new NotInitializedException("callback is empty");
        }

        /**
         * Must open the I/O.
         */
        if (!callback.notifyAfterThreadOpen()) {
            return;
        }

        try {
            while (!interrupted()) {
                if (!main()) {
                    break;
                }
            }
        } finally {
            /**
             * Must close the I/O which is opened.
             */
            callback.notifyBeforeThreadClose();
        }
    }

    /**
     * Main.
     * 
     * @return true, if successful
     */
    protected boolean main() {
        /**
         * Can be extended.
         */
        return idleStrategy.processItem(callback);
    }

    @Override
    public final int getThreadId() {
        return threadId;
    }

    /**
     * Sets the thread id.
     * 
     * @param value the new thread id
     */
    public final void setThreadId(final int value) {
        threadId = value;
    }

    @Override
    public final String getThreadName() {
        return getName();
    }

    /**
     * Sets the thread name.
     * 
     * @param name the name
     */
    public final void setThreadName(final String name) {
        setName(name);
    }

    @Override
    public final void startThread() {
        start();
    }

    @Override
    public final void joinThread() throws InterruptedException {
        join();
    }

    @Override
    public final void asynStop() {
        idleStrategy.setInterrupted(true);
        interrupt();
    }

    /**
     * Checks if is can stop.
     * 
     * @return true, if is can stop
     */
    public final boolean isCanStop() {
        return interrupted() || idleStrategy.isInterrupted();
    }

    /**
     * Gets the idle strategy.
     * 
     * @return the idle strategy
     */
    public final IdleStrategy getIdleStrategy() {
        return idleStrategy;
    }

    /**
     * Sets the idle strategy.
     * 
     * @param value the new idle strategy
     */
    public final void setIdleStrategy(final IdleStrategy value) {
        if (value != null) {
            idleStrategy = value;
        }
    }

    /**
     * Gets the callback.
     * 
     * @return the callback
     */
    public ThreadCallback getCallback() {
        return callback;
    }

    /**
     * Final set callback. Only can set one time.
     * 
     * @param value the value
     */
    public void finalSetCallback(final ThreadCallback value) {
        if (callback == null && value != null) {
            callback = value;
        }
    }

    /**
     * Sets the idle retry count.
     * 
     * @param idleParkMode the new idle park mode
     * @param retryCount the new idle retry count
     */
    public void setIdleStrategy(final ParkMode idleParkMode, final int retryCount) {
        setIdleStrategy(PollingFactory.updateIdleStrategy(idleStrategy, idleParkMode, retryCount));
    }

    @Override
    public void updateConfiguration(final Configuration config, final int hint) {
        /**
         * Can be extended.
         */
        if (config == null) {
            return;
        }

        if (ConfigHint.NATIVE_FILE == hint || ConfigHint.CLI_OVERRIDE == hint) {
            setIdleStrategy(PollingFactory.updateIdleStrategy(idleStrategy, config, hint));
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("CallbackIdleSleepThread [threadName=").append(getThreadName())
                .append(", threadId=").append(threadId)
                .append(", idleStrategy=").append(idleStrategy).append("]");
        return builder.toString();
    }

    /**
     * Instantiates a new callback idle sleep thread.
     */
    public CallbackIdleSleepThread() {
        super();
    }

    /**
     * Instantiates a new callback idle sleep thread.
     * 
     * @param name the name
     */
    public CallbackIdleSleepThread(final String name) {
        super();
        setName(name);
    }

    /**
     * Instantiates a new callback idle sleep thread.
     * 
     * @param id the id
     * @param name the name
     */
    public CallbackIdleSleepThread(final int id, final String name) {
        super();
        setThreadId(id);
        setName(name);
    }

    /** The idle strategy. */
    private IdleStrategy idleStrategy;

    /** The thread id. */
    private int threadId;

    /** The callback. DO NOT ADD to toString(). */
    private ThreadCallback callback;

    {
        // default is to loop sleep idle strategy.
        idleStrategy = new LoopSleepIdleStrategy();
    }
}
