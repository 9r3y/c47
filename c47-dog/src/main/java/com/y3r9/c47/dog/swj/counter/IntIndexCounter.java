package com.y3r9.c47.dog.swj.counter;

/**
 * The Interface IntIndexCounter.
 * 
 * @version 1.0
 * @since project 2.0
 */
public interface IntIndexCounter {

    /**
     * Gets counter value.
     * 
     * @param counterIndex the counter index
     * @return the counter value
     */
    int get(int counterIndex);

    /**
     * Count.
     * 
     * @param counterIndex the counter index
     * @param value the value
     */
    void count(int counterIndex, int value);

    /**
     * Gets the counter index max.
     * 
     * @return the counter index max
     */
    int getCounterSize();

    /**
     * Clear.
     */
    void clear();
}
