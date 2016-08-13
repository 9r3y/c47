package com.y3r9.c47.dog.swj.counter;

/**
 * The Interface LongIndexCounter.
 * 
 * @version 1.1
 * @since project 2.0
 */
public interface LongIndexCounter {

    /**
     * Gets counter value.
     * 
     * @param counterIndex the counter index
     * @return the counter value
     */
    long get(int counterIndex);

    /**
     * Count.
     * 
     * @param counterIndex the counter index
     * @param value the value
     */
    void count(int counterIndex, long value);

    /**
     * Gets the counter size.
     * 
     * @return the counter size
     */
    int getCounterSize();

    /**
     * Clear.
     */
    void clear();
}
