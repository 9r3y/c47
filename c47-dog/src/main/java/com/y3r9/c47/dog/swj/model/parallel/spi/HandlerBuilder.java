package com.y3r9.c47.dog.swj.model.parallel.spi;

/**
 * The Interface ParallelBuilder.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @since project 3.0
 */
public interface HandlerBuilder<D, C, R> {

    /**
     * Gets the join handler.
     * 
     * @return the join handler
     */
    JoinHandler<R> getJoinHandler();

    /**
     * Gets the partitioner.
     * 
     * @param dataType the data type
     * @return the partitioner
     */
    Partitioner<D> getPartitioner(ParallelDataType dataType);

    /**
     * Gets the span handler.
     * 
     * @return the span handler
     */
    DispatchSpanHandler<D> getSpanHandler();

    /**
     * Gets the partition context manager.
     * 
     * @param partId the part id
     * @param totalPartCount the total partition count
     * @return the partition context
     */
    C getPartitionContextManager(int partId, int totalPartCount);

    /**
     * Gets the work handler.
     * 
     * @param workId the work id
     * @return the work handler
     */
    WorkHandler<D, C, R> getWorkHandler(int workId);
}
