package com.y3r9.c47.dog.swj.model.collection;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type Lru hash map.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 * @version 1.0
 * @since v3.9
 */
public class LruHashMap<K, V> extends LinkedHashMap<K, V> {

    /** The constant DEFAULT_INITIAL_CAPACITY. */
    public static final int DEFAULT_INITIAL_CAPACITY = 16;

    /** The constant DEFAULT_LOAD_FACTOR. */
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    @Override
    public boolean removeEldestEntry(Map.Entry eldest) {
        return size() > maxEntries;
    }

    /**
     * Instantiates a new Lru hash map.
     *
     * @param entries the entries
     * @param initialCapacity the initial capacity
     * @param loadFactor the load factor
     */
    public LruHashMap(final int entries, final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor, true);
        maxEntries = entries;
    }

    /**
     * Instantiates a new Lru hash map.
     *
     * @param entries the entries
     * @param initialCapacity the initial capacity
     */
    public LruHashMap(final int entries, int initialCapacity) {
        this(entries, initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Instantiates a new Lru hash map.
     *
     * @param entries the entries
     */
    public LruHashMap(final int entries) {
        this(entries, DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /** The Max entries. */
    private final int maxEntries;
}
