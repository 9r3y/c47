package com.y3r9.c47.dog.swj.polling;

import org.apache.commons.configuration.Configuration;

import cn.com.netis.dp.commons.common.ConfigHint;
import cn.com.netis.dp.commons.lang.NegativeArgumentException;
import com.y3r9.c47.dog.swj.config.key.PollingKey;
import com.y3r9.c47.dog.swj.polling.spi.OverSizeStrategy;
import com.y3r9.c47.dog.swj.polling.spi.ParkMode;
import com.y3r9.c47.dog.swj.polling.spi.SizeProc;

/**
 * The Class BusyOverSizeStrategy.
 * 
 * @version 1.1
 * @see OverSizeStrategy
 * @since project 3.0
 * @since project 3.1 change name from NoSleepOverSizeBlocking to BusyOverSizeStrategy
 */
public class BusyOverSizeStrategy implements OverSizeStrategy {

    @Override
    public final void waitForOverflow(final SizeProc proc) {
        if (!isOverflowControlEnable()) {
            return;
        }

        while (isOverflow(proc)) {
            if (Thread.interrupted()) {
                return;
            }
        }
    }

    /**
     * Checks if is overflow.
     * 
     * @param proc the procedure
     * @return true, if is overflow
     */
    public final boolean isOverflow(final SizeProc proc) {
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
                "BusyOverSizeStrategy#setOverSizeRetryCount(int)");
    }

    @Override
    public final int getOverSizeRetryCount() {
        return -1;
    }

    @Override
    public final void setOverSizeBlockingNano(final long value) {
        throw new UnsupportedOperationException(
                "BusyOverSizeStrategy#setOverSizeBlockingNano(long)");
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
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BusyOverSizeStrategy [cacheSize=").append(cacheSize).append("]");
        return builder.toString();
    }

    @Override
    public ParkMode getOverSizeParkMode() {
        return ParkMode.busy;
    }

    /** The cache size. */
    private int cacheSize;

    {
        setCacheSize(DEFAULT_CACHE_SIZE);
    }

}
