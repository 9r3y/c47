package com.y3r9.c47.dog.swj.model.collection;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.tuple.Pair;

import cn.com.netis.dp.commons.common.ConfigHint;
import cn.com.netis.dp.commons.lang.NullArgumentException;
import com.y3r9.c47.dog.swj.model.collection.spi.PairDualQueue;
import com.y3r9.c47.dog.swj.model.collection.spi.PairDualQueueThread;
import com.y3r9.c47.dog.swj.polling.AbstractIdleSleepThread;
import com.y3r9.c47.dog.swj.polling.LoopSleepOverSizeStrategy;
import com.y3r9.c47.dog.swj.polling.PollingFactory;
import com.y3r9.c47.dog.swj.polling.spi.OverSizeStrategy;


/**
 * The Class AbstractBlockingPairDualQueueThread. This class contains a safe IN dual queue, whose IN
 * queue is <em>thread-safe</em> but OUT queue is <em>not</em>. The same time, if the dual queue is
 * full, the new incoming data will be blocked in the IN queue until the OUT queue has room for it.
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
 * </...>
 * 
 * </pre>
 * 
 * @param <H> the generic header type
 * @param <D> the generic data type
 * @version 2.19
 * @see AbstractIdleSleepThread, PairDualQueueThread
 * @since project 2.0
 * @since project 3.0 updates to 2.18
 * @since 2.18 extends AbstractIdleSleepThread
 */
public abstract class AbstractBlockingPairDualQueueThread<H, D> extends AbstractIdleSleepThread
        implements PairDualQueueThread<H, D> {

    @Override
    public final boolean processItem() {
        if (cacheQueue.isEmpty()) {
            return false;
        }

        // thread safe, for this work thread to drain data from the IN queue to the OUT queue
        cacheQueue.safeDrainInQueueToOut();

        while (!cacheQueue.isOutQueueEmpty()) {
            final Pair<H, D> item = cacheQueue.removeFromOutQueue();
            processItem(item.getLeft(), item.getRight());
        }
        return true;
    }

    /**
     * Process item.
     * 
     * @param header the header
     * @param data the data
     */
    protected abstract void processItem(H header, D data);

    @Override
    public final int getCacheQueueSize() {
        return cacheQueue.size();
    }

    @Override
    public final PairDualQueue<H, D> getCachePairQueue() {
        return cacheQueue;
    }

    @Override
    public void addItem(final H header, final D data) {
        /**
         * Can be extended.
         */

        // wait for cache size first
        overSizeStrategy.waitForOverflow(cacheQueue);

        // thread safe, for the other thread to add data to the IN queue
        cacheQueue.addToInQueue(header, data);
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
            setOverSizeStrategy(PollingFactory.updateOverSizeStrategy(overSizeStrategy, config,
                    hint));
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append(", cacheQueue=").append(cacheQueue)
                .append(", overSizeStrategy=").append(overSizeStrategy);
        return builder.toString();
    }

    /**
     * Gets the overflow strategy.
     * 
     * @return the overflow strategy
     */
    public final OverSizeStrategy getOverflowStrategy() {
        return overSizeStrategy;
    }

    /**
     * Sets the over size strategy.
     * 
     * @param value the new over size strategy
     */
    public void setOverSizeStrategy(final OverSizeStrategy value) {
        NullArgumentException.check(value, "overSizeStrategy");
        overSizeStrategy = value;
    }

    /**
     * Instantiates a new abstract blocking dual queue thread.
     * 
     * @param id the id
     * @param name the name
     */
    protected AbstractBlockingPairDualQueueThread(final int id, final String name) {
        super(id, name);
    }

    /**
     * Instantiates a new thread packet consumer.
     * 
     * @param name the name
     */
    protected AbstractBlockingPairDualQueueThread(final String name) {
        this(0, name);
    }

    /** The overflow strategy. @since 2.19 */
    private OverSizeStrategy overSizeStrategy;

    /** The cache queue. */
    private final transient PairDualQueue<H, D> cacheQueue;

    {
        // default is to sleep
        overSizeStrategy = new LoopSleepOverSizeStrategy();
        // it should always use safe IN dual queue.
        cacheQueue = new SafeInPairDualQueue<H, D>();
    }
}
