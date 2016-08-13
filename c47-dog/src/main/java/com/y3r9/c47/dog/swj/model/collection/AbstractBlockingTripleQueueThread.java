package com.y3r9.c47.dog.swj.model.collection;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.tuple.Pair;

import cn.com.netis.dp.commons.common.ConfigHint;
import cn.com.netis.dp.commons.lang.NullArgumentException;
import com.y3r9.c47.dog.swj.model.collection.spi.TripleQueue;
import com.y3r9.c47.dog.swj.model.collection.spi.TripleQueueThread;
import com.y3r9.c47.dog.swj.polling.LoopSleepOverSizeStrategy;
import com.y3r9.c47.dog.swj.polling.PollingFactory;
import com.y3r9.c47.dog.swj.polling.PushToProcessProvider;
import com.y3r9.c47.dog.swj.polling.spi.OverSizeStrategy;
import com.y3r9.c47.dog.swj.polling.spi.PushToProcess;

/**
 * The Class AbstractBlockingTripleQueueThread. This thread contains a triple queue instance as the
 * cache buffer. When the WAITING queue in the triple queue is full, it will drop the new incoming
 * data in WAITING queue until it has room.
 * 
 * <pre>
 * XML example for configuration:
 * 
 * <...>
 *   <cacheSize>200000</cacheSize>
 *   <idleParkMode>sleep</idleParkMode>
 *   <idleRetryCount>4</idleRetryCount>
 *   <ideaSleepNano>1</ideaSleepNano>
 *   <overSizeParkMode>sleep</overSizeParkMode>
 *   <overSizeRetryCount>8</overSizeRetryCount>
 *   <overSizeBlockingNano>1</overSizeBlockingNano>
 *   <pushToProcessSize>1000</pushToProcessSize>
 *   <pushToProcessMilli>100</pushToProcessMilli>
 * </...>
 * 
 * </pre>
 * 
 * @param <H> the generic header type
 * @param <D> the generic data type
 * 
 * @version 2.9
 * @see Thread, TripleQueueThread
 * @since project 2.0
 * @since project 3.0 updates to 2.8
 */
public abstract class AbstractBlockingTripleQueueThread<H, D> extends Thread implements
        TripleQueueThread<H, D> {

    @Override
    public final void startThread() {
        start();
    }

    @Override
    public final void joinThread() throws InterruptedException {
        join();
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

    @Override
    public void asynStop() {
        /**
         * Can be override.
         */
        interrupt();
    }

    @Override
    public final int getCacheQueueSize() {
        return cacheQueue.size();
    }

    @Override
    public final TripleQueue<H, D> getCacheQueue() {
        return cacheQueue;
    }

    @Override
    public final boolean addItem(final long time, final H header, final D data) {
        // check size
        overSizeStrategy.waitForOverflow(cacheQueue);

        if (pushToProcess.needPush(cacheQueue.getInQueueSize(), time)) {
            cacheQueue.safeDrainInQueueToMid();
        }

        // NOT thread safe, add to the queue directly
        cacheQueue.addToInQueue(header, data);
        return true;
    }

    @Override
    public final void flush() {
        cacheQueue.safeDrainInQueueToMid();
    }

    @Override
    public final boolean hasNextItem() {
        return !cacheQueue.isProcessQueueEmpty();
    }

    @Override
    public final Pair<H, D> removeItem() {
        if (cacheQueue.isOutQueueEmpty() && !cacheQueue.isMidQueueEmpty()) {
            cacheQueue.safeDrainMidQueueToOut();
        }

        // NOT thread safe, remove from the queue directly
        return cacheQueue.removeFromOutQueue();
    }

    @Override
    public final void triggerPushToProcess() {
        pushToProcess.asycPush();
    }

    /**
     * Gets the overflow strategy.
     * 
     * @return the overflow strategy
     */
    public final OverSizeStrategy getOverflowBlocking() {
        return overSizeStrategy;
    }

    /**
     * Sets the over size strategy.
     * 
     * @param value the new over size strategy
     */
    public final void setOverSizeStrategy(final OverSizeStrategy value) {
        NullArgumentException.check(value, "overSizeStrategy");
        overSizeStrategy = value;
    }

    /**
     * Gets the push to process.
     * 
     * @return the push to process
     */
    final PushToProcess getPushToProcess() {
        return pushToProcess;
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
            setOverSizeStrategy(PollingFactory.updateOverSizeStrategy(overSizeStrategy, config,
                    hint));
            pushToProcess.updateConfiguration(config, hint);
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append(", cacheQueue=").append(cacheQueue)
                .append(", overSizeStrategy=").append(overSizeStrategy)
                .append(", pushToProcess=").append(pushToProcess);
        return builder.toString();
    }

    /**
     * Instantiates a new thread packet consumer.
     * 
     * @param name the name
     */
    protected AbstractBlockingTripleQueueThread(final String name) {
        super();
        setName(name);
    }

    /** The thread id. */
    private int threadId;

    /** The over size strategy. @since 2.9 */
    private OverSizeStrategy overSizeStrategy;

    /** The push to process. @since 2.8 */
    private final PushToProcess pushToProcess;

    /** The cache queue. */
    private final transient TripleQueue<H, D> cacheQueue;

    {
        // default to sleep
        overSizeStrategy = new LoopSleepOverSizeStrategy();
        pushToProcess = new PushToProcessProvider();
        // queue.
        cacheQueue = new TripleQueueProvider<H, D>();
    }
}
