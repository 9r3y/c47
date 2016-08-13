package com.y3r9.c47.dog.swj.polling;

import org.apache.commons.configuration.Configuration;

import com.y3r9.c47.dog.swj.config.key.PollingKey;
import com.y3r9.c47.dog.swj.polling.spi.IdleStrategy;
import com.y3r9.c47.dog.swj.polling.spi.OverSizeStrategy;
import com.y3r9.c47.dog.swj.polling.spi.ParkMode;

/**
 * A factory for creating Polling objects.
 * 
 * @version 1.2
 * @since project 3.0
 * @since project 3.1 support idle park mode
 * @since project 3.1 support over size park mode
 */
public final class PollingFactory {

    /**
     * Creates a new idle strategy.
     *
     * @param idleParkMode the idle park mode
     * @param retryCount the retry count
     * @return the idle strategy
     */
    public static IdleStrategy
            createIdleStrategy(final ParkMode idleParkMode, final int retryCount) {
        if (retryCount < 0) {
            // for project 3.0 compatible
            return new BusyIdleStrategy();
        }

        switch (idleParkMode) {
        case sleep:
            return retryCount == 1 ? new SleepIdleStrategy()
                    : new LoopSleepIdleStrategy(retryCount);
        case yield:
            return retryCount == 1 ? new YieldIdleStrategy()
                    : new LoopYieldIdleStrategy(retryCount);
        case busy:
            return new BusyIdleStrategy();
        default:
            throw new IllegalArgumentException(String.format(
                    "PollingFactory#createIdleStrategy(%s,%d)", idleParkMode.name(), retryCount));
        }

    }

    /**
     * Update idle strategy.
     *
     * @param value the idle strategy
     * @param idleParkMode the idle park mode
     * @param retryCount the retry count
     * @return the idle sleep
     */
    public static IdleStrategy updateIdleStrategy(final IdleStrategy value,
            final ParkMode idleParkMode, final int retryCount) {
        final boolean sameStrategy = (retryCount == value.getIdleRetryCount()
                && idleParkMode.equals(value.getIdleParkMode()));
        return sameStrategy ? value : createIdleStrategy(idleParkMode, retryCount);
    }

    /**
     * Update idle strategy.
     * 
     * @param value the value
     * @param config the config
     * @param hint the hint
     * @return the idle strategy
     */
    public static IdleStrategy updateIdleStrategy(final IdleStrategy value,
            final Configuration config, final int hint) {
        final boolean strategyChanged = config.containsKey(PollingKey.idleRetryCount.name())
                || config.containsKey(PollingKey.idleParkMode.name());
        if (strategyChanged) {
            // for idle strategy changed
            final ParkMode idleParkMode = ParkMode.valueOf(config.getString(
                    PollingKey.idleParkMode.name(), value.getIdleParkMode().name()));
            final int retryCount = config.getInt(
                    PollingKey.idleRetryCount.name(), value.getIdleRetryCount());

            final IdleStrategy result = updateIdleStrategy(value, idleParkMode, retryCount);
            result.updateConfiguration(config, hint);
            return result;
        }

        // for idle strategy not changed
        value.updateConfiguration(config, hint);
        return value;

    }

    /**
     * Creates a new over size strategy.
     *
     * @param overSizeParkMode the over size park mode
     * @param retryCount the retry count
     * @return the over size strategy
     */
    public static OverSizeStrategy createOverSizeStrategy(final ParkMode overSizeParkMode,
            final int retryCount) {
        if (retryCount < 0) {
            return new BusyOverSizeStrategy();
        }

        switch (overSizeParkMode) {
        case sleep:
            return retryCount == 1 ? new SleepOverSizeStrategy()
                    : new LoopSleepOverSizeStrategy(retryCount);
        case yield:
            return retryCount == 1 ? new YieldOverSizeStrategy()
                    : new LoopYieldOverSizeStrategy(retryCount);
        case busy:
            return new BusyOverSizeStrategy();
        default:
            throw new IllegalArgumentException(String.format(
                    "PollingFactory#createOverSizeStrategy(%s,%d)",
                    overSizeParkMode.name(), retryCount));
        }

    }

    /**
     * Update over size strategy.
     *
     * @param value the value
     * @param overSizeParkMode the over size park mode
     * @param retryCount the retry count
     * @return the over size strategy
     */
    public static OverSizeStrategy updateOverSizeStrategy(final OverSizeStrategy value,
            final ParkMode overSizeParkMode, final int retryCount) {

        final boolean sameStrategy = (retryCount == value.getOverSizeRetryCount()
                && overSizeParkMode.equals(value.getOverSizeParkMode()));

        return sameStrategy ? value : createOverSizeStrategy(overSizeParkMode, retryCount);
    }

    /**
     * Update over size strategy.
     * 
     * @param value the value
     * @param config the config
     * @param hint the hint
     * @return the over size strategy
     */
    public static OverSizeStrategy updateOverSizeStrategy(final OverSizeStrategy value,
            final Configuration config, final int hint) {

        final boolean strategyChanged = config.containsKey(PollingKey.overSizeRetryCount.name())
                || config.containsKey(PollingKey.overSizeParkMode.name());

        if (strategyChanged) {
            final ParkMode overSizeParkMode = ParkMode.valueOf(config.getString(
                    PollingKey.overSizeParkMode.name(), value.getOverSizeParkMode().name()));

            final int retryCount = config.getInt(PollingKey.overSizeRetryCount.name(),
                    value.getOverSizeRetryCount());
            final OverSizeStrategy result = updateOverSizeStrategy(
                    value, overSizeParkMode, retryCount);
            result.updateConfiguration(config, hint);
            return result;
        }
        value.updateConfiguration(config, hint);
        return value;

    }

    /**
     * Instantiates a new polling factory.
     */
    private PollingFactory() {
    }
}
