package com.y3r9.c47.dog.swj.model.parallel.spi;

import cn.com.netis.dp.commons.common.statis.DropObservable;
import com.y3r9.c47.dog.swj.model.sort.spi.SortPipePut;

/**
 * The Interface WorkHandler.
 * 
 * @version 1.0
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @see DropObservable
 * @since project 3.0
 */
public interface WorkHandler<D, C, R> {
    /**
     * Gets the handler name.
     * 
     * @return the handler name
     */
    String getHandlerName();

    /**
     * Consume work job. To apply this function, must make {@link #getJoinPipe()} ready.
     * 
     * @param token the token
     * @param data the data
     * @param context the context
     */
    void consumeWorkJob(long token, D data, C context);

    /**
     * Sets the join pipe.
     * 
     * @param joinPipe the new join pipe
     */
    void setJoinPipe(SortPipePut<R> joinPipe);

    /**
     * Gets the join pipe.
     * 
     * @return the join pipe
     */
    SortPipePut<R> getJoinPipe();

    /**
     * Notify after thread open.
     * 
     * @return true, if successful
     */
    boolean notifyAfterThreadOpen();

    /**
     * Notify before thread close.
     * 
     * @return true, if successful
     */
    boolean notifyBeforeThreadClose();
}
