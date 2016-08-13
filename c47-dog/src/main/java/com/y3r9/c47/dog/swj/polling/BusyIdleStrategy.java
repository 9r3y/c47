package com.y3r9.c47.dog.swj.polling;

import com.y3r9.c47.dog.swj.polling.spi.IdleStrategy;
import com.y3r9.c47.dog.swj.polling.spi.ParkMode;
import com.y3r9.c47.dog.swj.polling.spi.ThreadCallback;
import org.apache.commons.configuration.Configuration;

/**
 * The Class BusyIdleStrategy.
 *
 * @version 1.1
 * @see IdleStrategy
 * @since project 3.1 change name from NoSleepIdleSleep to BusyIdleStrategy
 */
public class BusyIdleStrategy implements IdleStrategy {

    @Override
    public final boolean processItem(final ThreadCallback proc) {
        proc.processItem();
        return !isInterrupted();
    }

    @Override
    public final void setIdleRetryCount(final int value) {
        throw new UnsupportedOperationException("BusyIdleStrategy#setIdleSleepRetryCount(int)");
    }

    @Override
    public final int getIdleRetryCount() {
        return -1;
    }

    @Override
    public final void setIdeaSleepNano(final long value) {
        throw new UnsupportedOperationException("BusyIdleStrategy#setIdeaSleepNano(long)");
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
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BusyIdleStrategy [interrupted=").append(interrupted).append("]");
        return builder.toString();
    }

    @Override
    public void updateConfiguration(final Configuration config, final int hint) {
        // dummy
    }

    @Override
    public ParkMode getIdleParkMode() {
        return ParkMode.busy;
    }

    @Override
    public long getIdleCount() {
        // do not idle
        return 0;
    }

    @Override
    public long getIdleTimeCost() {
        // do not idle
        return 0;
    }

    /** The interrupted. */
    private boolean interrupted;
}
