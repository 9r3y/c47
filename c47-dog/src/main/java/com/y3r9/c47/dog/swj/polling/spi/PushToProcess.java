package com.y3r9.c47.dog.swj.polling.spi;

import cn.com.netis.dp.commons.common.Configurable;

/**
 * The Interface PushToProcess.
 * 
 * <pre>
 * XML example for configuration:
 * 
 * <...>
 *   <pushToProcessSize>1000</pushToProcessSize>
 *   <pushToProcessMilli>100</pushToProcessMilli>
 * </...>
 * 
 * </pre>
 * 
 * @version 1.0
 * @see Configurable
 * @since project 3.0
 */
public interface PushToProcess extends Configurable {

    /** The default push to process size. */
    int DEFAULT_PUSH_TO_PROCESS_SIZE = 1000;

    /** The default push to process millisecond. */
    int DEFAULT_PUSH_TO_PROCESS_MILLI = 100;

    /**
     * Need push.
     * 
     * @param cacheSize the cache size
     * @param time the time
     * @return true, if successful
     */
    boolean needPush(int cacheSize, long time);

    /**
     * Checks if is size push.
     * 
     * @param size the size
     * @return true, if is size push
     */
    boolean isSizePush(int size);

    /**
     * Checks if is time push.
     * 
     * @param base the base
     * @param time the time
     * @return true, if is time push
     */
    boolean isTimePush(long base, long time);

    /**
     * Asyc push.
     */
    void asycPush();

    /**
     * Sets the push to process size.
     * 
     * @param value the new push to process size
     */
    void setPushToProcessSize(int value);

    /**
     * Gets the push to process size.
     * 
     * @return the push to process size
     */
    int getPushToProcessSize();

    /**
     * Sets the push to process milliseconds.
     * 
     * @param value the new push to process milliseconds
     */
    void setPushToProcessMilli(int value);

    /**
     * Gets the push to process milliseconds.
     * 
     * @return the push to process milliseconds
     */
    int getPushToProcessMilli();

    /**
     * Gets the push to process nanoseconds.
     * 
     * @return the push to process nanoseconds
     */
    long getPushToProcessNano();
}
