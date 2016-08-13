package com.y3r9.c47.dog.swj.model.parallel.spi;

import cn.com.netis.dp.commons.common.Node;
import cn.com.netis.dp.commons.common.statis.DropObservable;

/**
 * The Interface JoinHandler.
 * 
 * @param <R> the generic result data type
 * 
 * @version 1.0
 * @see Node
 * @since project 3.0
 */
public interface JoinHandler<R> extends Node {

    /**
     * Process join result.
     * 
     * @param resultData the result data
     */
    void consumeJoinResult(R resultData);

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

    /**
     * Wait for inband terminate.
     * 
     * @param timeoutSecond the timeout second
     * @return true, if terminate detected. false, if timeout
     */
    boolean waitForInbandTerminate(int timeoutSecond);

    /**
     * Gets the drop observer.
     * 
     * @return the drop observer
     */
    DropObservable getDropObserver();
}
