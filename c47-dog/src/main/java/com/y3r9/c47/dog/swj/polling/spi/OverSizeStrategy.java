package com.y3r9.c47.dog.swj.polling.spi;

import cn.com.netis.dp.commons.common.Configurable;

/**
 * The Interface OverSizeStrategy.
 * 
 * <pre>
 * XML example for configuration:
 * 
 * <...>
 *   <cacheSize>50000</cacheSize>
 *   <overSizeParkMode>sleep</overSizeParkMode>
 *   <overSizeRetryCount>4</overSizeRetryCount>
 *   <overSizeBlockingNano>1</overSizeBlockingNano>
 * </...>
 * 
 * </pre>
 * 
 * @version 1.2
 * @see Configurable
 * @since project 3.0
 * @since project 3.1 change name from OverSizeBlocking to OverSizeStrategy
 */
public interface OverSizeStrategy extends Configurable {

    /** The default cache size. */
    int DEFAULT_CACHE_SIZE = 50000;

    /** The default over size retry count. */
    int DEFAULT_OVER_SIZE_RETRY_COUNT = 4;

    /** The default over size blocking nanosecond. */
    long DEFAULT_OVER_SIZE_BLOCKING_NANO = 1;

    /**
     * Wait for overflow.
     * 
     * @param proc the size procedure
     */
    void waitForOverflow(SizeProc proc);

    /**
     * Sets the cache size.
     * 
     * @param value the new cache size
     */
    void setCacheSize(int value);

    /**
     * Gets the cache size.
     * 
     * @return the cache size
     */
    int getCacheSize();

    /**
     * Sets the over size retry count. <code>-1</code> never sleeping. <code>0</code> for retrying
     * the count of Integer.MAX_VALUE.
     * 
     * @param value the new over size retry count
     */
    void setOverSizeRetryCount(int value);

    /**
     * Gets the over size retry count. <code>-1</code> never sleeping. <code>0</code> for retrying
     * the count of Integer.MAX_VALUE.
     * 
     * @return the over size retry count
     */
    int getOverSizeRetryCount();

    /**
     * Sets the over size blocking nanosecond.
     * 
     * @param value the new over size blocking nanosecond
     */
    void setOverSizeBlockingNano(long value);

    /**
     * Gets the over size blocking nanosecond.
     * 
     * @return the over size blocking nanosecond
     */
    long getOverSizeBlockingNano();

    /**
     * Gets the park mode for over size.
     *
     * @return the park mode for over size
     * @since 1.2
     */
    ParkMode getOverSizeParkMode();
}
