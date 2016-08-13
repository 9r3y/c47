package com.y3r9.c47.dog.swj.counter;

import cn.com.netis.dp.commons.common.statis.ByteStatis;

/**
 * The Interface ByteCounter.
 * 
 * @version 1.0
 * @see ByteStatis
 * @since project 2.2
 */
public interface ByteCounter extends ByteStatis {

    /**
     * Update.
     * 
     * @param time the time
     * @param incBytes the incremental bytes
     */
    void updateCounter(long time, long incBytes);

    /**
     * Update.
     * 
     * @param time the time
     * @param incCounts the incremental counts
     * @param incBytes the incremental bytes
     */
    void updateCounter(long time, long incCounts, long incBytes);

    /**
     * Reset counter.
     */
    void resetCounter();

    /**
     * Sets the counts.
     * 
     * @param value the new counts
     */
    void setCounts(long value);

    /**
     * Sets the bytes.
     * 
     * @param value the new bytes
     */
    void setBytes(long value);

    /**
     * Sets the start time.
     * 
     * @param time the new start time
     */
    void setStartTime(long time);

    /**
     * Sets the end time.
     * 
     * @param time the new end time
     */
    void setEndTime(long time);
}
