package com.y3r9.c47.dog.swj.model.parallel.spi;

/**
 * The Interface DispatchSpanHandler.
 * 
 * @param <D> the generic type
 * @version 1.0
 * @since project 3.0
 */
public interface DispatchSpanHandler<D> {

    /**
     * Span data.
     * 
     * @param data the data
     * @param proc the dispatch procedure
     * @param partitionCount the partition count
     */
    void spanData(D data, DispatchProc<D> proc, int partitionCount);
}
