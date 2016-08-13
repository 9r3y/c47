package com.y3r9.c47.dog.swj.polling;

import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.Configuration;

import cn.com.netis.dp.commons.common.ConfigHint;
import cn.com.netis.dp.commons.lang.NegativeArgumentException;
import com.y3r9.c47.dog.swj.config.key.PollingKey;
import com.y3r9.c47.dog.swj.polling.spi.OverSizeStrategy;
import com.y3r9.c47.dog.swj.polling.spi.ParkMode;
import com.y3r9.c47.dog.swj.polling.spi.SizeProc;

/**
 * The Class SleepOverSizeStrategy.
 * 
 * @version 1.1
 * @see OverSizeStrategy
 * @since project 3.0
 * @since project 3.1 change name from SleepOverSizeBlocking to SleepOverSizeStrategy
 */
public class SleepOverSizeStrategy implements OverSizeStrategy {

    @Override
    public final void waitForOverflow(final SizeProc proc) {
        if (!isOverflowControlEnable()) {
            return;
        }

        while (isOverflow(proc)) {
            if (!blockingSleep()) {
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
        if (Thread.interrupted()) {
            return false;
        }
        return proc.size() >= cacheSize;
    }

    /**
     * Blocking sleep.
     * 
     * @return <code>true</code> if continue, <code>false</code> if interrupted.
     */
    protected final boolean blockingSleep() {
        // sleep
        try {
            // use nanoseconds for performance
            TimeUnit.NANOSECONDS.sleep(overSizeBlockingNano);
        } catch (InterruptedException ignore) {
            // stop process when the thread is interrupted
            return false;
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
        throw new UnsupportedOperationException(
                "SleepOverSizeStrategy#setOverSizeRetryCount(int)");
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
        builder.append("SleepOverSizeStrategy [cacheSize=").append(cacheSize)
                .append(", overSizeBlockingNano=").append(overSizeBlockingNano).append("]");
        return builder.toString();
    }

    @Override
    public ParkMode getOverSizeParkMode() {
        return ParkMode.sleep;
    }

    /**
     * Instantiates a new sleep over size blocking.
     */
    public SleepOverSizeStrategy() {
        setCacheSize(DEFAULT_CACHE_SIZE);
        setOverSizeBlockingNano(DEFAULT_OVER_SIZE_BLOCKING_NANO);
    }

    /** The cache size. */
    private int cacheSize;

    /** The blocking sleep nanosecond. */
    private long overSizeBlockingNano;

}
