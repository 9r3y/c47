package com.y3r9.c47.dog.swj.polling;

import cn.com.netis.dp.commons.common.ConfigHint;
import cn.com.netis.dp.commons.lang.NegativeArgumentException;
import com.y3r9.c47.dog.swj.config.key.PollingKey;
import com.y3r9.c47.dog.swj.polling.spi.IdleStrategy;
import com.y3r9.c47.dog.swj.polling.spi.ParkMode;
import com.y3r9.c47.dog.swj.polling.spi.ThreadCallback;
import org.apache.commons.configuration.Configuration;

/**
 * The Class LoopYieldIdleStrategy.
 *
 * @version 1.1
 * @see IdleStrategy
 * @since project 3.1
 */
public class LoopYieldIdleStrategy implements IdleStrategy {

    @Override
    public final boolean processItem(final ThreadCallback proc) {
        for (int i = 0; i < idleRetryCount; i++) {
            if (isInterrupted()) {
                return false;
            }
            if (proc.processItem()) {
                return true;
            }
        }

        idleCount++;
        final long nanoBeforeIdle = System.nanoTime();
        // yield
        Thread.yield();
        idleTimeCost += System.nanoTime() - nanoBeforeIdle;
        return true;
    }

    @Override
    public final void setIdleRetryCount(final int value) {
        NegativeArgumentException.check(value, "idleRetryCount");
        idleRetryCount = value == 0 ? Integer.MAX_VALUE : value;
    }

    @Override
    public final int getIdleRetryCount() {
        return idleRetryCount;
    }

    @Override
    public final void setIdeaSleepNano(final long value) {
        throw new UnsupportedOperationException("LoopYieldIdleStrategy#setIdeaSleepNano(int)");
    }

    @Override
    public final long getIdleSleepNano() {
        return 0;
    }

    @Override
    public final void setInterrupted(final boolean value) {
        interrupted = value;
    }

    @Override
    public final boolean isInterrupted() {
        return interrupted;
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
            setIdleRetryCount(config.getInt(PollingKey.idleRetryCount.name(),
                    idleRetryCount));
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("LoopYieldIdleStrategy [idleRetryCount=").append(idleRetryCount)
                .append(", interrupted=").append(interrupted).append("]");
        return builder.toString();
    }

    @Override
    public ParkMode getIdleParkMode() {
        return ParkMode.yield;
    }

    @Override
    public long getIdleCount() {
        return idleCount;
    }

    @Override
    public long getIdleTimeCost() {
        return idleTimeCost;
    }

    /**
     * Instantiates a new loop yield idle strategy.
     */
    public LoopYieldIdleStrategy() {
        this(DEFAULT_IDLE_RETRY_COUNT);
    }

    /**
     * Instantiates a new loop yield idle strategy.
     *
     * @param retryCount the retry count
     */
    public LoopYieldIdleStrategy(final int retryCount) {
        setIdleRetryCount(retryCount);
    }

    /** The Idle count. */
    private long idleCount;

    /** The Idle time cost. */
    private long idleTimeCost;

    /** The idle retry count. */
    private int idleRetryCount;

    /** The interrupted. */
    private boolean interrupted;

}
