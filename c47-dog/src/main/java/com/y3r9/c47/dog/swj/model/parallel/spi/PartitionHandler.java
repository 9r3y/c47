package com.y3r9.c47.dog.swj.model.parallel.spi;

/**
 * The Interface PartitionHandler.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 * @since project 3.0
 */
public interface PartitionHandler<D, C, R> {

    /**
     * Try consume partition. For the consumer to pop and consume data with respect to the context.
     * In multiple threads application, it will use tryLock() to lock the resource first.
     * 
     * @param selector the selector used to select the data
     * @param proc the process
     * @param threToken the threshold token
     * @return the item count processed, <code>-1</code> for tryLock() return false.
     */
    int tryConsumePartition(int selector, WorkHandler<D, C, R> proc, long threToken);

    /**
     * Consume partition. For the consumer to pop and consume data with respect to the context.
     * Other than tryConsumePartition, resource will be not be locked.
     *
     * @param selector the selector
     * @param proc the proc
     * @param threToken the thre token
     * @return the int
     */
    int consumePartition(int selector, WorkHandler<D, C, R> proc, long threToken);
}
