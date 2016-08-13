package com.y3r9.c47.dog.swj.model.parallel.spi;

/**
 * The Interface DispatchProc.
 * 
 * @param <D> the generic type
 * @version 1.0
 * @since project 3.0
 */
public interface DispatchProc<D> {

    /**
     * Dispatch data.
     * 
     * @param data the data
     */
    void dispatchData(D data);
}
