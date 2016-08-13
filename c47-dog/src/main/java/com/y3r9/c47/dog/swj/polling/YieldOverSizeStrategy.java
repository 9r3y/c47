package com.y3r9.c47.dog.swj.polling;

import org.apache.commons.configuration.Configuration;

import cn.com.netis.dp.commons.common.ConfigHint;
import cn.com.netis.dp.commons.lang.NegativeArgumentException;
import com.y3r9.c47.dog.swj.config.key.PollingKey;
import com.y3r9.c47.dog.swj.polling.spi.OverSizeStrategy;
import com.y3r9.c47.dog.swj.polling.spi.ParkMode;
import com.y3r9.c47.dog.swj.polling.spi.SizeProc;

/**
 * The Class YieldOverSizeStrategy.
 * 
 * @version 1.0
 * @see OverSizeStrategy
 * @since project 3.1
 */
public class YieldOverSizeStrategy implements OverSizeStrategy {

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
        if (Thread.interrupted()) {
            return false;
        }
        return proc.size() >= cacheSize;
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
        throw new UnsupportedOperationException(
                "YieldOverSizeStrategy#setOverSizeRetryCount(int)");
    }

    @Override
    public final int getOverSizeRetryCount() {
        return 1;
    }

    @Override
    public final void setOverSizeBlockingNano(final long value) {
        NegativeArgumentException.check(value, "overSizeBlockingNano");
        overSizeBlockingNano = value;
    }

    @Override
    public final long getOverSizeBlockingNano() {
        return overSizeBlockingNano;
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
            setOverSizeBlockingNano(config.getLong(PollingKey.overSizeBlockingNano.name(),
                    overSizeBlockingNano));
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("YieldOverSizeStrategy [cacheSize=").append(cacheSize)
                .append(", overSizeBlockingNano=").append(overSizeBlockingNano).append("]");
        return builder.toString();
    }

    @Override
    public ParkMode getOverSizeParkMode() {
        return ParkMode.yield;
    }

    /**
     * Instantiates a new sleep over size blocking.
     */
    public YieldOverSizeStrategy() {
        setCacheSize(DEFAULT_CACHE_SIZE);
        setOverSizeBlockingNano(DEFAULT_OVER_SIZE_BLOCKING_NANO);
    }

    /** The cache size. */
    private int cacheSize;

    /** The blocking sleep nanosecond. */
    private long overSizeBlockingNano;
    
}
