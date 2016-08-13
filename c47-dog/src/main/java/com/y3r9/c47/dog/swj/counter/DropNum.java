package com.y3r9.c47.dog.swj.counter;

/**
 * The Class DropNum.
 * 
 * @version 1.1
 * @since project 3.0
 * @since project 3.2 updates 1.1
 */
public class DropNum {

    /**
     * Gets the count.
     * 
     * @return the count
     */
    public final long getCount() {
        return count;
    }

    /**
     * Sets the count.
     *
     * @param value the new count
     * @since 1.1
     */
    public final void setCount(final long value) {
        count = value;
    }

    /**
     * Adds the count.
     * 
     * @param dropCount the drop count
     */
    public final void addCount(final long dropCount) {
        count += dropCount;
    }

    /**
     * Store count to last.
     *
     * @since 1.1
     */
    public final void storeCountToLast() {
        lastCount = count;
    }

    /**
     * Gets the last count.
     *
     * @return the last count
     * @since 1.1
     */
    public final long getLastCount() {
        return lastCount;
    }

    /**
     * Sets the last count.
     *
     * @param value the new last count
     * @since 1.1
     */
    public final void setLastCount(final long value) {
        lastCount = value;
    }

    /**
     * Gets the delta count.
     *
     * @return the delta count
     * @since 1.1
     */
    public final long getDeltaCount() {
        return count - lastCount;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DropNum [count=").append(count).append(", lastCount=")
                .append(lastCount).append("]");
        return builder.toString();
    }

    /** The count. */
    private long count;

    /** The last cache. */
    private long lastCount;
}
