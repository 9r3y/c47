package com.y3r9.c47.dog.swj.counter;

/**
 * The Interface DoubleIndexCounter.
 * 
 * @version 1.0
 * @since project 2.0
 */
public interface DoubleIndexCounter {

    /**
     * Gets the.
     * 
     * @param counterIndex the counter index
     * @return the double
     */
    double get(int counterIndex);

    /**
     * Count.
     * 
     * @param counterIndex the counter index
     * @param value the value
     */
    void count(int counterIndex, double value);

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
