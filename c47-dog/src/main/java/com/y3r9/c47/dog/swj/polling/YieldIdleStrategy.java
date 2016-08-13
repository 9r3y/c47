package com.y3r9.c47.dog.swj.polling;

import com.y3r9.c47.dog.swj.polling.spi.IdleStrategy;
import com.y3r9.c47.dog.swj.polling.spi.ParkMode;
import com.y3r9.c47.dog.swj.polling.spi.ThreadCallback;
import org.apache.commons.configuration.Configuration;

/**
 * The Class YieldIdleStrategy.
 *
 * @version 1.1
 * @see IdleStrategy
 * @since project 3.1
 */
public class YieldIdleStrategy implements IdleStrategy {

    @Override
    public final boolean processItem(final ThreadCallback proc) {
        if (proc.processItem()) {
            return true;
        }

        // yield
        idleCount++;
        final long nanoBeforeIdle = System.nanoTime();
        Thread.yield();
        idleTimeCost += System.nanoTime() - nanoBeforeIdle;
        return true;
    }

    @Override
    public final void setIdleRetryCount(final int value) {
        throw new UnsupportedOperationException("YieldIdleStrategy#setIdleSleepRetryCount(int)");
    }

    @Override
    public final int getIdleRetryCount() {
        return 1;
    }

    @Override
    public final void setIdeaSleepNano(final long value) {
        throw new UnsupportedOperationException("YieldIdleStrategy#setIdleSleepRetryCount(int)");
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
        // dummy
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("YieldIdleStrategy [interrupted=").append(interrupted).append("]");
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

    /** The Idle count. */
    private long idleCount;

    /** The Idle time cost. */
    private long idleTimeCost;

    /** The interrupted. */
    private boolean interrupted;

}
