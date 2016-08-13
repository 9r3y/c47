package com.y3r9.c47.dog.swj.model.sort.spi;

import java.util.List;

/**
 * The Interface SortPipe.
 * 
 * @param <V> the value type
 * @version 1.0
 * @see SortPipePut, SortPipeStatus
 * @since project 3.0
 */
public interface SortPipe<V> extends SortPipePut<V>, SortPipeStatus {

    /** The default capability. */
    int DEFAULT_CAPABILITY = 65536;

    /**
     * Sets the capability.
     * 
     * @param value the new capability
     */
    void setCapability(int value);

    /**
     * Pop.
     * 
     * @return the v
     */
    V pop();

    /**
     * Pop.
     * 
     * @param result the result
     */
    void pop(List<V> result);
}
