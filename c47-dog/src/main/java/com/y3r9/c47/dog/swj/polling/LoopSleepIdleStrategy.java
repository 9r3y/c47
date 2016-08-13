package com.y3r9.c47.dog.swj.polling;

import java.util.concurrent.TimeUnit;

import cn.com.netis.dp.commons.common.ConfigHint;
import cn.com.netis.dp.commons.lang.NegativeArgumentException;
import com.y3r9.c47.dog.swj.config.key.PollingKey;
import com.y3r9.c47.dog.swj.polling.spi.IdleStrategy;
import com.y3r9.c47.dog.swj.polling.spi.ParkMode;
import com.y3r9.c47.dog.swj.polling.spi.ThreadCallback;
import org.apache.commons.configuration.Configuration;

/**
 * The Class LoopSleepIdleStrategy.
 *
 * @version 1.1
 * @see IdleStrategy
 * @since project 3.1 change name from LoopIdleSleep to LoopSleepIdleStrategy
 */
public class LoopSleepIdleStrategy implements IdleStrategy {

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

        // sleep
        return idleSleep();
    }

    /**
     * Idle sleep.
     *
     * @return <code>true</code> if continue, <code>false</code> if interrupted.
     */
    protected final boolean idleSleep() {
        idleCount++;
        // sleep
        try {
            final long nanoBeforeIdle = System.nanoTime();
            // use nanoseconds for performance
            TimeUnit.NANOSECONDS.sleep(idleSleepNano);
            idleTimeCost += System.nanoTime() - nanoBeforeIdle;
        } catch (InterruptedException ignore) {
            // stop process when the thread is interrupted
            interrupted = true;
            return false;
        }
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
        NegativeArgumentException.check(value, "idleSleepNano");
        idleSleepNano = value == 0 ? Integer.MAX_VALUE : value;
    }

    @Override
    public final long getIdleSleepNano() {
        return idleSleepNano;
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
            setIdeaSleepNano(config.getLong(PollingKey.idleSleepNano.name(), idleSleepNano));
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("LoopSleepIdleStrategy [idleRetryCount=").append(idleRetryCount)
                .append(", idleSleepNano=").append(idleSleepNano)
                .append(", interrupted=").append(interrupted).append("]");
        return builder.toString();
    }

    @Override
    public ParkMode getIdleParkMode() {
        return ParkMode.sleep;
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
     * Instantiates a new loop sleep idle strategy.
     */
    public LoopSleepIdleStrategy() {
        this(DEFAULT_IDLE_RETRY_COUNT);
    }

    /**
     * Instantiates a new loop sleep idle strategy.
     *
     * @param retryCount the retry count
     */
    public LoopSleepIdleStrategy(final int retryCount) {
        setIdleRetryCount(retryCount);
        setIdeaSleepNano(DEFAULT_IDLE_SLEEP_NANO);
    }

    /** The Idle count. */
    private long idleCount;

    /** The Idle time cost. */
    private long idleTimeCost;

    /** The idle retry count. */
    private int idleRetryCount;

    /** The idle sleep nanosecond. */
    private long idleSleepNano;

    /** The interrupted. */
    private boolean interrupted;

}
