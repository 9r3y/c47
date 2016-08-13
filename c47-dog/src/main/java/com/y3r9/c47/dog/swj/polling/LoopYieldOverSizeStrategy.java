package com.y3r9.c47.dog.swj.polling;

import org.apache.commons.configuration.Configuration;

import cn.com.netis.dp.commons.common.ConfigHint;
import cn.com.netis.dp.commons.lang.NegativeArgumentException;
import com.y3r9.c47.dog.swj.config.key.PollingKey;
import com.y3r9.c47.dog.swj.polling.spi.OverSizeStrategy;
import com.y3r9.c47.dog.swj.polling.spi.ParkMode;
import com.y3r9.c47.dog.swj.polling.spi.SizeProc;

/**
 * The Class LoopYieldOverSizeStrategy.
 * 
 * @version 1.0
 * @see OverSizeStrategy
 * @since project 3.1
 */
public class LoopYieldOverSizeStrategy implements OverSizeStrategy {

    @Override
    public final void waitForOverflow(final SizeProc proc) {
        if (!isOverflowControlEnable()) {
            return;
        }

        while (isOverflow(proc)) {
            Thread.yield();
        }
    }

    /**
     * Checks if is overflow.
     * 
     * @param proc the procedure
     * @return true, if is overflow
     */
    public final boolean isOverflow(final SizeProc proc) {
        for (int i = 0; i < overSizeRetryCount; i++) {
            if (Thread.interrupted()) {
                return false;
            }
            if (proc.size() < cacheSize) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final void setCacheSize(final int value) {
        NegativeArgumentException.check(value, "cacheSize");
        cacheSize = value;
    }

    @Override
    public final int getCacheSize() {
        return cacheSize;
    }

    /**
     * Checks if is overflow control enable.
     * 
     * @return true, if is overflow control enable
     */
    public final boolean isOverflowControlEnable() {
        return cacheSize != 0;
    }

    @Override
    public final void setOverSizeRetryCount(final int value) {
        NegativeArgumentException.check(value, "overSizeRetryCount");
        overSizeRetryCount = value == 0 ? Integer.MAX_VALUE : value;
    }

    @Override
    public final int getOverSizeRetryCount() {
        return overSizeRetryCount;
    }

    @Override
    public final void setOverSizeBlockingNano(final long value) {
        throw new UnsupportedOperationException(
                "LoopYieldOverSizeStrategy#setOverSizeBlockingNano(long)");
    }

    @Override
    public final long getOverSizeBlockingNano() {
        return 0;
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
            setCacheSize(config.getInt(PollingKey.cacheSize.name(), cacheSize));
            setOverSizeRetryCount(config.getInt(PollingKey.overSizeRetryCount.name(),
                    overSizeRetryCount));
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("LoopYieldOverSizeStrategy [cacheSize=").append(cacheSize)
                .append(", overSizeRetryCount=").append(overSizeRetryCount).append("]");
        return builder.toString();
    }

    @Override
    public ParkMode getOverSizeParkMode() {
        return ParkMode.yield;
    }
    
    /**
     * Instantiates a new sleep over size strategy.
     */
    public LoopYieldOverSizeStrategy() {
        this(DEFAULT_OVER_SIZE_RETRY_COUNT);
    }

    /**
     * Instantiates a new sleep over size strategy.
     * 
     * @param retryCount the retry count
     */
    public LoopYieldOverSizeStrategy(final int retryCount) {
        setCacheSize(DEFAULT_CACHE_SIZE);
        setOverSizeRetryCount(retryCount);
    }

    /** The cache size. */
    private int cacheSize;

    /** The over size retry count. */
    private int overSizeRetryCount;

}
