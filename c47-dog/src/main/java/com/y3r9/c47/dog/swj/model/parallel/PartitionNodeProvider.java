package com.y3r9.c47.dog.swj.model.parallel;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.tuple.Pair;

import cn.com.netis.dp.commons.common.ConfigHint;
import com.y3r9.c47.dog.swj.config.key.PollingKey;
import com.y3r9.c47.dog.swj.model.collection.SafeInPairDualQueue;
import com.y3r9.c47.dog.swj.model.parallel.spi.WorkHandler;

/**
 * The Class PartitionNodeProvider.
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 * @see AbstractPartitionNode
 * @since project 2.0
 */
public class PartitionNodeProvider<D, C, R> extends AbstractPartitionNode<D, C, R> {

    @Override
    public final int tryConsumePartition(final int selector, final WorkHandler<D, C, R> proc,
            final long threToken) {
        // thread safe
        if (!lock.tryLock()) {
            return -1;
        }

        try {
            return consume(proc, threToken);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public final int consumePartition(final int selector, final WorkHandler<D, C, R> proc,
            final long threToken) {
        return consume(proc, threToken);
    }

    /**
     * Consume.
     *
     * @param proc the process
     * @param threToken the threshold token
     * @return the integer for result
     */
    protected final int consume(final WorkHandler<D, C, R> proc, final long threToken) {
        // thread safe, for this work thread to drain data from the IN queue to the OUT queue
        if (cacheQueue.isOutQueueEmpty()) {
            cacheQueue.safeDrainInQueueToOut();
        }

        int result = 0;
        C context = getContext();

        // work till the threshold token
        while (!cacheQueue.isOutQueueEmpty()) {
            final long token = cacheQueue.peekOutQueueForHeader();

            // use "large equal than" comparison.
            // for the case that dispatch token is start with 0 now.
            if (token >= threToken) {
                break;
            }

            final D item = cacheQueue.removeOutQueueForData();
            proc.consumeWorkJob(token, item, context);
            result++;

            // break out then other partition has chance to process
            // breakProcessSize == 0 disable this feature
            if (breakProcessSize != 0 && result >= getBreakProcessSize()) {
                break;
            }
        }

        return result;
    }

    @Override
    public final void addItem(final long token, final D data) {
        // thread safe, for the other thread to add data to the IN queue
        cacheQueue.addToInQueue(token, data);
    }

    @Override
    public final boolean markEmpty() {
        return cacheQueue.markEmpty();
    }

    @Override
    public final boolean unmarkEmptyAddItem(final long token, final D data) {
        return cacheQueue.unmarkEmptyAddToInQueue(token, data);

    }

    @Override
    public final int getCacheQueueSize() {
        return cacheQueue.size();
    }

    @Override
    public final long getCacheQueueHeadToken() {
        Pair<Long, D> data = cacheQueue.peekFromOutQueue();
        if (data != null) {
            return data.getLeft();
        }
        data = cacheQueue.peekFromInQueue();
        if (data != null) {
            return data.getLeft();
        }
        return -1;
    }

    @Override
    public final boolean isConsuming() {
        return lock.isLocked();
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
            if (config.containsKey(PollingKey.breakProcessSize.name())) {
                breakProcessSize = config.getInt(PollingKey.breakProcessSize.name());
            }
        }
    }

    /**
     * Sets the break process size.
     *
     * @param value the new break process size
     */
    public void setBreakProcessSize(final int value) {
        breakProcessSize = value;
    }

    /**
     * Gets the break process size.
     *
     * @return the break process size
     */
    public int getBreakProcessSize() {
        return breakProcessSize;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PartitionNodeProvider, [").append(super.toString())
                .append(", cacheQueue=").append(cacheQueue)
                .append(", breakProcessSize=").append(breakProcessSize).append("]");
        return builder.toString();
    }

    /**
     * Instantiates a new partition node provider.
     */
    public PartitionNodeProvider() {
        super();
    }

    /**
     * Instantiates a new partition node provider.
     *
     * @param id the id
     */
    public PartitionNodeProvider(final int id) {
        super(id);
    }

    /**
     * Instantiates a new partition node provider.
     *
     * @param id the id
     * @param title the title
     */
    public PartitionNodeProvider(final int id, final String title) {
        super(id, title);
    }

    /** The lock. */
    private final transient ReentrantLock lock;

    /** The cache queue. */
    private final transient SafeInPairDualQueue<Long, D> cacheQueue;

    /** The break process size. */
    private int breakProcessSize;

    {
        lock = new ReentrantLock();
        // it should always use safe IN dual queue.
        cacheQueue = new SafeInPairDualQueue<>();
    }
}
