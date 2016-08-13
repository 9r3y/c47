package com.y3r9.c47.dog.swj.model.parallel.spi;

import cn.com.netis.dp.commons.common.Configurable;
import cn.com.netis.dp.commons.common.Node;
import cn.com.netis.dp.commons.common.statis.JoinNodeObservable;
import com.y3r9.c47.dog.swj.model.sort.spi.SortPipe;
import com.y3r9.c47.dog.swj.polling.spi.ParkMode;

/**
 * The Interface JoinNode.
 * <p>
 * <pre>
 * XML example for configuration:
 *
 * <joinNode>
 *   <idleParkMode>sleep</idleParkMode>
 *   <idleRetryCount>16</idleRetryCount>
 *   <ideaSleepNano>1</ideaSleepNano>
 * </joinNode>
 *
 * </pre>
 *
 * @param <R> the generic result data type
 * @version 1.1
 * @see Node, Configurable
 * @since project 3.0
 */
public interface JoinNode<R> extends Node, Configurable, JoinNodeObservable {

    /** The default join node idle retry count. */
    int DEFAULT_JOIN_NODE_IDLE_RETRY_COUNT = 4;

    /** The default join node idle park mode. */
    ParkMode DEFAULT_JOIN_NODE_IDLE_PARK_MODE = ParkMode.sleep;

    /**
     * Sets the join pipe capability.
     *
     * @param value the new join pipe capability
     */
    void setJoinPipeCapability(int value);

    /**
     * Gets the join pipe capability.
     *
     * @return the join pipe capability
     */
    int getJoinPipeCapability();

    /**
     * Gets the join pipe.
     *
     * @return the join pipe
     */
    SortPipe<R> getJoinPipe();

    /**
     * Sets the join handler.
     *
     * @param value the new join handler
     */
    void setJoinHandler(JoinHandler<R> value);

    /**
     * Gets the join handler.
     *
     * @return the join handler
     */
    JoinHandler<R> getJoinHandler();

    /**
     * Sets the join pipe.
     *
     * @param value the new join pipe
     */
    void setJoinPipe(SortPipe<R> value);
}
