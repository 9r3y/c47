package com.y3r9.c47.dog.swj.model.sort.spi;

import org.apache.commons.lang3.tuple.Pair;

/**
 * The Interface SortQueue.
 * 
 * @param <H> the generic type
 * @param <D> the generic type
 * 
 * @version 1.1
 * @since project 2.3
 */
public interface SortQueue<H, D> {

    /**
     * Checks for next.
     * 
     * @return true, if successful
     */
    boolean hasSortedNext();

    /**
     * Next.
     * 
     * @return the pair
     */
    Pair<H, D> next();

    /**
     * Adds the item.
     * 
     * @param header the header
     * @param data the data
     */
    void addItem(H header, D data);

    /**
     * Gets the cache size.
     * 
     * @return the cache size
     */
    int getCacheSize();

    /**
     * Sets the partition count.
     * 
     * @param value the new partition count
     */
    void setPartitionCount(int value);

    /**
     * Gets the partition count.
     * 
     * @return the partition count
     */
    int getPartitionCount();

    /**
     * Report.
     * 
     * @param message the message
     * @since 1.1
     */
    void report(StringBuilder message);
}
