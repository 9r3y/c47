package com.y3r9.c47.dog.swj.model.sort.spi;

/**
 * The Interface SortPipe.
 * 
 * @version 1.0
 * @param <V> the value type
 * @see SortPipe
 * @since project 3.0
 */
public interface CheckPutSortPipe<V> extends SortPipe<V> {

    /** The default retry count before sleep. */
    int DEFAULT_RETRY_COUNT_BEFORE_SLEEP = 4;

    /** The default blocking sleep nanosecond. */
    long DEFAULT_WAITING_LOCK_SLEEP_NANO = 1;

    /**
     * Sets the waiting lock sleep nanosecond.
     * 
     * @param value the new waiting lock sleep nanosecond
     */
    void setWaitingLockSleepNano(long value);

    /**
     * Gets the waiting lock sleep nanosecond.
     * 
     * @return the waiting lock sleep nanosecond
     */
    long getWaitingLockSleepNano();

    /**
     * Sets the retry count before sleep.
     * 
     * @param value the new retry count before sleep
     */
    void setRetryCountBeforeSleep(int value);

    /**
     * Gets the retry count before sleep.
     * 
     * @return the retry count before sleep
     */
    int getRetryCountBeforeSleep();
}
