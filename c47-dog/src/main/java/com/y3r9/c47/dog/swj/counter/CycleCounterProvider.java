package com.y3r9.c47.dog.swj.counter;

import java.util.concurrent.TimeUnit;

import cn.com.netis.dp.commons.lang.Constants;
import cn.com.netis.dp.commons.lang.NonPositiveArgumentException;

/**
 * The Class CycleCounterProvider.
 * 
 * Internal package only.
 * 
 * @version 1.2
 * @see ByteCounterProvider
 * @see CycleCounter
 * @since project 2.0
 */
final class CycleCounterProvider extends ByteCounterProvider implements
        CycleCounter {

    /** The Constant DEFAULT_CYCLE_LENGTH. */
    private static final long DEFAULT_CYCLE_LENGTH = 50000;

    /** The Constant TO_SECOND. */
    private static final double MILLI_TO_SECOND = TimeUnit.SECONDS.toMillis(1);

    @Override
    public void updateCounter(final long timestamp, final long incBytes) {
        super.updateCounter(timestamp, incBytes);
        distance--;
    }

    @Override
    public void updateCounter(final long timestamp, final long incCounts, final long incBytes) {
        super.updateCounter(timestamp, incCounts, incBytes);
        distance--;
    }

    @Override
    public void resetCounter() {
        super.resetCounter();
        nextCycle();
    }

    @Override
    public boolean isCycleEnd() {
        return distance <= 0;
    }

    @Override
    public void nextCycle() {
        distance = cycleLength;
        baseBytes = getBytes();
        lastSystemTime = System.currentTimeMillis();
    }

    @Override
    public double getDeltaSystemTime() {
        return (System.currentTimeMillis() - lastSystemTime) / MILLI_TO_SECOND;
    }

    @Override
    public long getDeltaByteCount() {
        return getBytes() - baseBytes;
    }

    @Override
    public void setCycleLength(final long value) {
        NonPositiveArgumentException.check(value, "cycle length");
        cycleLength = value;
    }

    @Override
    public long getCycleLength() {
        return cycleLength;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(Constants.STRING_BUILDER_MIDDLE_SIZE);
        builder.append("CycleCounterProvider [cycleLength=").append(cycleLength)
                .append(", distance=").append(distance).append(", baseBytes=")
                .append(baseBytes).append(", lastSystemTime=").append(lastSystemTime)
                .append(", toString()=").append(super.toString()).append(']');
        return builder.toString();
    }

    /** The packet period. */
    private long cycleLength = DEFAULT_CYCLE_LENGTH;

    /** The distance. */
    private transient long distance;

    /** The base bytes. */
    private transient long baseBytes;

    /** The last system time. */
    private transient long lastSystemTime = System.currentTimeMillis();
}
