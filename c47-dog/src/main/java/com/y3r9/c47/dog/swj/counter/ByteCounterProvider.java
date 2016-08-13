package com.y3r9.c47.dog.swj.counter;

import com.y3r9.c47.dog.swj.value.Timestamp;

import cn.com.netis.dp.commons.lang.Constants;

/**
 * The Class ByteCounterProvider.
 * 
 * @version 1.1
 * @see ByteCounter
 * @since project 2.2
 */
public class ByteCounterProvider implements ByteCounter {

    /** The Constant UNINITIALIZED_TIME. */
    private static final int UNINITIALIZED_TIME = 0;

    @Override
    public void updateCounter(final long time, final long incBytes) {
        /**
         * Will be extended.
         */

        counts++;
        bytes += incBytes;

        if (startTime == UNINITIALIZED_TIME) {
            startTime = time;
        }
        lastTime = time;
    }

    @Override
    public void updateCounter(final long time, final long incCount, final long incBytes) {
        /**
         * Will be extended.
         */

        counts += incCount;
        bytes += incBytes;

        if (startTime == UNINITIALIZED_TIME) {
            startTime = time;
        }
        lastTime = time;
    }

    @Override
    public void resetCounter() {
        /**
         * Will be extended.
         */
        counts = 0;
        bytes = 0;
        startTime = UNINITIALIZED_TIME;
        lastTime = 0;
    }

    @Override
    public final long getCounts() {
        return counts;
    }

    @Override
    public final long getBytes() {
        return bytes;
    }

    @Override
    public final long getStartTime() {
        return startTime;
    }

    @Override
    public final long getLastTime() {
        return lastTime;
    }

    @Override
    public final long getDuration() {
        return lastTime - startTime;
    }

    @Override
    public final double getDurationSeconds() {
        return Timestamp.toDoubleSeconds(getDuration());
    }

    @Override
    public final void setCounts(final long value) {
        counts = value;

    }

    @Override
    public final void setBytes(final long value) {
        bytes = value;
    }

    @Override
    public final void setStartTime(final long time) {
        startTime = time;
    }

    @Override
    public final void setEndTime(final long time) {
        lastTime = time;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(Constants.STRING_BUILDER_MIDDLE_SIZE);
        builder.append("ByteCounterProvider [counts=").append(counts).append(", bytes=")
                .append(bytes).append(", startTime=").append(startTime).append(", lastTime=")
                .append(lastTime).append(", getDurationSeconds()=").append(getDurationSeconds())
                .append(']');
        return builder.toString();
    }

    /** The counts. */
    private long counts;

    /** The bytes. */
    private long bytes;

    /** The start time. */
    private long startTime;

    /** The last time. */
    private transient long lastTime;

}
