package com.y3r9.c47.dog.swj.model.sort;

import java.util.List;

import com.y3r9.c47.dog.swj.model.sort.spi.SortPipe;

/**
 * The Class SingleInSortPipeQueue.
 * 
 * @param <V> the value type
 * @version 1.0
 * @since project 3.0
 */
public class SingleInSortPipeQueue<V> {

    /**
     * Size.
     * 
     * @return the int
     */
    public final int size() {
        return (int) (token - cache.getOutToken());
    }

    /**
     * Pop.
     * 
     * @return the v
     */
    public final V pop() {
        V result = cache.pop();
        while (result == null) {
            result = cache.pop();
        }
        return result;
    }

    /**
     * Pop.
     * 
     * @param result the result
     */
    public final void pop(final List<V> result) {
        cache.pop(result);
    }

    /**
     * Put.
     * 
     * @param value the value
     */
    public final void put(final V value) {
        while (true) {
            if (token <= cache.getInTokenThre()) {
                break;
            }
        }

        // dispatch it directly or put into queue
        cache.put(token, value);

        // increment the token
        token++;
    }

    /** The token. */
    private transient long token;

    /** The cache. */
    private final transient SortPipe<V> cache;

    {
        cache = new NativeSortPipeProvider<>();
    }
}
