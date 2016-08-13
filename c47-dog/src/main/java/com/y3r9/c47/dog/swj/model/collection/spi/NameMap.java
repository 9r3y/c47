package com.y3r9.c47.dog.swj.model.collection.spi;

import java.util.Map;

/**
 * The Interface NameMap.
 * 
 * @param <V> the value type
 * 
 * @version 1.0
 * @since project 2.0
 */
public interface NameMap<V> extends Map<String, V> {

    /**
     * Put.
     * 
     * @param system the system
     * @param group the group
     * @param name the name
     * @param value the value
     */
    void put(String system, String group, String name, V value);

    /**
     * Put.
     * 
     * @param system the system
     * @param name the name
     * @param value the value
     */
    void put(String system, String name, V value);

    /**
     * Gets the backend.
     * 
     * @param system the system
     * @param group the group
     * @param name the name
     * @return the export
     */
    V get(String system, String group, String name);

    /**
     * Gets the.
     * 
     * @param system the system
     * @param name the name
     * @return the v
     */
    V get(String system, String name);
}
