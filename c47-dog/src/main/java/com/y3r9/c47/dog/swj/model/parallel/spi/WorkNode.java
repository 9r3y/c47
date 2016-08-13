package com.y3r9.c47.dog.swj.model.parallel.spi;

import cn.com.netis.dp.commons.common.Configurable;
import cn.com.netis.dp.commons.common.Node;
import cn.com.netis.dp.commons.common.statis.WorkNodeObservable;
import com.y3r9.c47.dog.swj.model.sort.spi.SortPipePut;
import com.y3r9.c47.dog.swj.polling.spi.ParkMode;

/**
 * The Interface WorkNode.
 * <pre>
 * XML example for configuration:
 * <joinNode>
 *   <idleParkMode>sleep</idleParkMode>
 *   <idleRetryCount>8</idleRetryCount>
 *   <ideaSleepNano>1</ideaSleepNano>
 * </joinNode>
 * </pre>
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.2
 * @see Node, Configurable
 * @since project 3.0
 */
public interface WorkNode<D, C, R> extends Node, Configurable, WorkNodeObservable {

    /** The default work node idle retry count. */
    int DEFAULT_WORK_NODE_IDLE_RETRY_COUNT = 8;

    /** The default work node idle park mode. */
    ParkMode DEFAULT_WORK_NODE_IDLE_PARK_MODE = ParkMode.sleep;

    /**
     * Sets the work handler.
     *
     * @param value the value
     */
    void setWorkHandler(WorkHandler<D, C, R> value);

    /**
     * Gets the work handler.
     *
     * @return the work handler
     */
    WorkHandler<D, C, R> getWorkHandler();

    /**
     * Sets partition node scheduler.
     *
     * @param value the value
     */
    void setPartScheduler(final PartitionScheduler<D, C, R> value);

    /**
     * Gets part scheduler.
     *
     * @return the partition handler
     */
    PartitionScheduler<D, C, R> getPartScheduler();

    /**
     * Sets the join pipe put.
     *
     * @param value the new join pipe put
     */
    void setJoinPipePut(SortPipePut<R> value);

    /**
     * Gets the join pipe put.
     *
     * @return the join pipe put
     */
    SortPipePut<R> getJoinPipePut();

    /**
     * Sets initial selector.
     *
     * @param value the value
     */
    void setSelector(final int value);

    /**
     * Reverse selector direction.
     */
    void reverseSelectorDirection();
}
