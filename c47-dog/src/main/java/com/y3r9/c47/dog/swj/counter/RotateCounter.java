package com.y3r9.c47.dog.swj.counter;

/**
 * The Class RotateCounter.
 * 
 * @version 2.0
 */
public final class RotateCounter {

    /**
     * Gets the count.
     * 
     * @return the count
     */
    public int get() {
        return count;
    }

    /**
     * Sets the count.
     * 
     * @param value the value
     */
    public int set(final int value) {
        if (value >= 0) {
            count = value;
        } else {
            count = 0;
        }
        return count;
    }

    /**
     * Inc the count.
     * 
     * @return the count
     */
    public int inc() {
        return set(count + 1);
    }

    public int inc(final int value) {
        return set(count + value);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("RotateCounter [count=").append(count).append("]");
        return builder.toString();
    }

    /**
     * Instantiates a new rotate positive integer.
     */
    public RotateCounter() {
    }

    /**
     * Instantiates a new rotate positive integer.
     * 
     * @param value the value
     */
    public RotateCounter(final int value) {
        set(value);
    }

    /** The count. */
    private int count;
}
