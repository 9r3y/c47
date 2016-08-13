package com.y3r9.c47.dog.swj.model.sort;

import java.util.concurrent.TimeUnit;

import cn.com.netis.dp.commons.lang.NegativeArgumentException;
import com.y3r9.c47.dog.swj.model.sort.spi.CheckPutSortPipe;

/**
 * The Class SortPipeProvider.
 * 
 * @param <V> the value type
 * @version 1.0
 * @see SortPipeProvider, CheckPutSortPipe
 * @since project 3.0
 */
public class AtomicCheckPutSortPipeProvider<V> extends AtomicSortPipeProvider<V> implements
        CheckPutSortPipe<V> {

    @Override
    public final void put(final long token, final V value) {
        do {
            if (tryCheckAndPut(token, value)) {
                break;
            }
            if (retryCountBeforeSleep > 0 && !waitingLockSleep()) {
                break;
            }
        } while (true);
    }

    /**
     * Try check and put.
     * 
     * @param token the token
     * @param value the value
     * @return true, if successful
     */
    private boolean tryCheckAndPut(final long token, final V value) {
        final int retryCount = retryCountBeforeSleep == 0 ? 1 : retryCountBeforeSleep;
        for (int i = 0; i < retryCount; i++) {

            if (token < getInTokenThre()) {
                doPutValue(token, value);
                // put value successfully
                return true;
            }
        }
        return false;
    }

    @Override
    public final void setWaitingLockSleepNano(final long value) {
        NegativeArgumentException.check(value, "waitingLockSleepNano");
        waitingLockSleepNano = value;
    }

    @Override
    public final long getWaitingLockSleepNano() {
        return waitingLockSleepNano;
    }

    @Override
    public final void setRetryCountBeforeSleep(final int value) {
        NegativeArgumentException.check(value, "retryCountBeforeSleep");
        retryCountBeforeSleep = value;
    }

    @Override
    public final int getRetryCountBeforeSleep() {
        return retryCountBeforeSleep;
    }

    /**
     * Waiting lock sleep.
     * 
     * @return true, if successful
     */
    private boolean waitingLockSleep() {
        try {
            TimeUnit.NANOSECONDS.sleep(waitingLockSleepNano);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * Instantiates a new abstract token sort.
     * 
     * @param size the size
     */
    public AtomicCheckPutSortPipeProvider(final int size) {
        super(size);
    }

    /**
     * Instantiates a new token pool provider.
     */
    public AtomicCheckPutSortPipeProvider() {
        this(DEFAULT_CAPABILITY);
    }

    /** The waiting lock sleep nanosecond. */
    private long waitingLockSleepNano;

    /** The retry count before sleep. */
    private int retryCountBeforeSleep;

    {
        setWaitingLockSleepNano(DEFAULT_WAITING_LOCK_SLEEP_NANO);
        setRetryCountBeforeSleep(DEFAULT_RETRY_COUNT_BEFORE_SLEEP);
    }
}
