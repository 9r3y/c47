package com.y3r9.c47.dog.swj.model.collection.spi;

/**
 * The Interface HotColdMap.
 * 
 * @param <K> the key type
 * @param <V> the value type
 * @version 1.0
 * @since project 2.0
 */
public interface HotColdMap<K, V> extends java.util.Map<K, V> {

    /** The default hot capacity. */
    int DEFAULT_HOT_CAPACITY = 10000;

    /**
     * Hot size.
     * 
     * @return the hot map size
     */
    int hotSize();

    /**
     * Cold size.
     * 
     * @return the cold map size
     */
    int coldSize();

    /**
     * Gets the hot capacity.
     * 
     * @return the hot capacity
     */
    int getHotCapacity();

    /**
     * Sets the hot capacity.
     * 
     * @param value the new hot capacity
     */
    void setHotCapacity(int value);
}
