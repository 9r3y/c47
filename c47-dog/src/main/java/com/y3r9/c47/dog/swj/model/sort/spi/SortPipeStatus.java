package com.y3r9.c47.dog.swj.model.sort.spi;

/**
 * The Interface SortPipeStatus.
 * 
 * @version 1.0
 * @see SortPipeThreshold
 * @since project 3.0
 */
public interface SortPipeStatus extends SortPipeThreshold {

    /**
     * Capability.
     * 
     * @return the capability
     */
    int capability();

    /**
     * Gets the out token.
     * 
     * @return the out token
     */
    long getOutToken();
}
