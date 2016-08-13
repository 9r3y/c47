package com.y3r9.c47.dog.swj.polling;

import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.Configuration;

import cn.com.netis.dp.commons.common.ConfigHint;
import cn.com.netis.dp.commons.lang.Constants;
import cn.com.netis.dp.commons.lang.NonPositiveArgumentException;
import com.y3r9.c47.dog.swj.config.key.PollingKey;
import com.y3r9.c47.dog.swj.polling.spi.PushToProcess;

/**
 * The Class PushToProcessProvider.
 * 
 * @version 1.0
 * @see PushToProcess
 * @since project 3.0
 */
public class PushToProcessProvider implements PushToProcess {

    @Override
    public final boolean needPush(final int cacheSize, final long time) {
        if (asycFlag) {
            lastPushToProcessTime = time;
            asycFlag = false;
            return true;
        }

        // apply size push
        if (isSizePush(cacheSize)) {
            lastPushToProcessTime = time;
            return true;
        }

        // apply time push
        if (isTimePush(lastPushToProcessTime, time)) {
            lastPushToProcessTime = time;
            return true;
        }

        return false;
    }

    @Override
    public final boolean isSizePush(final int size) {
        return size >= pushToProcessSize;
    }

    @Override
    public final boolean isTimePush(final long base, final long time) {
        return time >= base + pushToProcessNano;
    }

    @Override
    public final void asycPush() {
        asycFlag = true;
    }

    @Override
    public final void setPushToProcessSize(final int value) {
        NonPositiveArgumentException.check(value, "pushToProcessSize");
        pushToProcessSize = value;
    }

    @Override
    public final int getPushToProcessSize() {
        return pushToProcessSize;
    }

    @Override
    public final void setPushToProcessMilli(final int value) {
        NonPositiveArgumentException.check(value, "pushToProcessMilli");
        pushToProcessNano = TimeUnit.MILLISECONDS.toNanos(value);
    }

    @Override
    public final int getPushToProcessMilli() {
        return (int) TimeUnit.NANOSECONDS.toMillis(pushToProcessNano);
    }

    @Override
    public final long getPushToProcessNano() {
        return pushToProcessNano;
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
            setPushToProcessSize(config.getInt(PollingKey.pushToProcessSize.toString(),
                    pushToProcessSize));
            setPushToProcessMilli(config.getInt(PollingKey.pushToProcessMilli.toString(),
                    getPushToProcessMilli()));
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(Constants.STRING_BUILDER_MIDDLE_SIZE);
        builder.append("PushToProcessProvider [pushToProcessSize=").append(pushToProcessSize)
                .append(", pushToProcessNano=").append(pushToProcessNano)
                .append(", lastPushToProcessTime=").append(lastPushToProcessTime).append(']');
        return builder.toString();
    }

    /** The push to process size. */
    private int pushToProcessSize;

    /** The push to process nanoseconds. */
    private transient long pushToProcessNano;

    /** The last push to process time. */
    private transient long lastPushToProcessTime;

    /** The asyc flag. */
    private transient boolean asycFlag;

    {
        setPushToProcessSize(DEFAULT_PUSH_TO_PROCESS_SIZE);
        setPushToProcessMilli(DEFAULT_PUSH_TO_PROCESS_MILLI);
    }
}
