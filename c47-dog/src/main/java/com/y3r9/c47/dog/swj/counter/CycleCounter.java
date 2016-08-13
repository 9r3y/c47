package com.y3r9.c47.dog.swj.counter;

/**
 * The Interface CycleCounter.
 * 
 * @version 1.3
 * @see ByteCounter
 * @since project 2.0
 * @since 1.3 rename to CycleCounter
 */
public interface CycleCounter extends ByteCounter {

    /**
     * Checks if is cycle end.
     * 
     * @return true, if is cycle end
     */
    boolean isCycleEnd();

    /**
     * Next cycle.
     */
    void nextCycle();

    /**
     * Gets the delta system time. Unit is second.
     * 
     * @return the delta time
     */
    double getDeltaSystemTime();

    /**
     * Gets the delta byte count.
     * 
     * @return the delta byte count
     */
    long getDeltaByteCount();

    /**
     * Sets the cycle length.
     * 
     * @param value the new cycle length
     */
    void setCycleLength(long value);

    /**
     * Gets the cycle length.
     * 
     * @return the cycle length
     */
    long getCycleLength();
}
