package com.y3r9.c47.dog.swj.model.sort.spi;

/**
 * The Interface SortPipeThreshold.
 * 
 * @version 1.0
 * @since project 3.0
 */
public interface SortPipeThreshold {

    /**
     * Gets the in token threshold . This value equals to {link #getOutToken()} + {line
     * #capability()}.
     * 
     * @return the input threshold token
     */
    long getInTokenThre();
}
