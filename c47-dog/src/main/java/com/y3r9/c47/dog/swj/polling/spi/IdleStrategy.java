package com.y3r9.c47.dog.swj.polling.spi;

import cn.com.netis.dp.commons.common.Configurable;

/**
 * The Interface IdleStrategy.
 * 
 * <pre>
 * XML example for configuration:
 * 
 * <...>
 *   <idleParkMode>sleep</idleParkMode>
 *   <idleRetryCount>8</idleRetryCount>
 *   <ideaSleepNano>1</ideaSleepNano>
 * </...>
 * 
 * </pre>
 *
 * @version 1.3
 * @see Configurable
 * @since project 3.0
 */
public interface IdleStrategy extends Configurable {

    /** The default idle retry count. */
    int DEFAULT_IDLE_RETRY_COUNT = 8;

    /** The default idle sleep nanosecond. */
    long DEFAULT_IDLE_SLEEP_NANO = 1;

    /**
     * Process item.
     * 
     * @param proc the procedure
     * @return <code>true</code> if continue to process, <code>false</code> if <code>break</code> is
     *         set.
     */
    boolean processItem(ThreadCallback proc);

    /**
     * Sets the idle retry count. <code>-1</code> never sleeping. <code>0</code> for retrying the
     * count of Integer.MAX_VALUE.
     * 
     * @param value the new idle retry count
     */
    void setIdleRetryCount(int value);

    /**
     * Gets the idle retry count. <code>-1</code> for never sleeping. <code>0</code> for retrying
     * the count of Integer.MAX_VALUE.
     * 
     * @return the idle retry count
     */
    int getIdleRetryCount();

    /**
     * Sets the idle sleep nanosecond.
     * 
     * @param value the new idle sleep nanosecond
     */
    void setIdeaSleepNano(long value);

    /**
     * Gets the idle sleep nanosecond.
     * 
     * @return the idle sleep nanosecond
     */
    long getIdleSleepNano();

    /**
     * Sets the interrupted.
     * 
     * @param value the new interrupted
     */
    void setInterrupted(boolean value);

    /**
     * Checks if is interrupted.
     * 
     * @return true, if is interrupted
     */
    boolean isInterrupted();

    /**
     * Gets the park mode for idle.
     *
     * @return the park mode for idle
     * @since 1.2
     */
    ParkMode getIdleParkMode();

    /**
     * Gets idle count.
     *
     * @return the idle count
     * @since 1.3
     */
    long getIdleCount();

    /**
     * Gets idle time cost.
     *
     * @return the idle time cost
     * @since 1.3
     */
    long getIdleTimeCost();
}
