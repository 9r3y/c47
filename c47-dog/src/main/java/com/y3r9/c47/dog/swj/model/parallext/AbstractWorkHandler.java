package com.y3r9.c47.dog.swj.model.parallext;

import cn.com.netis.dp.commons.common.packet.PacketDropType;
import cn.com.netis.dp.commons.common.statis.DropCounter;
import cn.com.netis.dp.commons.common.statis.DropObservable;
import cn.com.netis.dp.commons.common.statis.ThroughputObservable;
import com.y3r9.c47.dog.swj.counter.DropCounterProvider;
import com.y3r9.c47.dog.swj.model.parallel.spi.WorkHandler;
import com.y3r9.c47.dog.swj.model.sort.spi.SortPipePut;

/**
 * The Class AbstractWorkHandler.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @see WorkHandler, ThroughputObservable
 * @since project 3.0
 */
public abstract class AbstractWorkHandler<D, C, R> implements WorkHandler<D, C, R>,
        ThroughputObservable {

    @Override
    public final void consumeWorkJob(final long token, final D data, final C context) {
        // main function, must cache all exceptions
        final R resultData = execute(data, context);

        // put into join pipe anyway
        putDataToJoinPipe(token, resultData);
    }

    @Override
    public final void setJoinPipe(final SortPipePut<R> value) {
        joinPipe = value;
    }

    @Override
    public final SortPipePut<R> getJoinPipe() {
        return joinPipe;
    }

    @Override
    public final String getHandlerName() {
        return handlerName;
    }

    @Override
    public final long getThroughputPacketsCount() {
        return throughputPacketsCount;
    }

    @Override
    public final long getThroughputBytesCount() {
        return throughputBytesCount;
    }

    /**
     * Update packets.
     * 
     * @param count the count
     */
    protected void updatePackets(final long count) {
        throughputPacketsCount += count;
    }

    /**
     * Update bytes.
     * 
     * @param count the count
     */
    protected void updateBytes(final long count) {
        throughputBytesCount += count;
    }

    /**
     * Execute.
     * 
     * @param data the data
     * @param context the context
     * @return the result data
     */
    protected abstract R execute(D data, C context);

    /**
     * Put data to join pipe.
     * 
     * @param token the token
     * @param resultData the result data
     */
    protected final void putDataToJoinPipe(final long token, final R resultData) {
        joinPipe.put(token, resultData);
    }

    @Override
    public boolean notifyAfterThreadOpen() {
        /**
         * Can be extended.
         */
        return true;
    }

    @Override
    public boolean notifyBeforeThreadClose() {
        /**
         * Can be extended.
         */
        return true;
    }

    /**
     * Gets the drop observer.
     * 
     * @return the drop observer
     */
    public final DropObservable getDropObserver() {
        return dropCounter;
    }

    /**
     * Adds the drop count.
     * 
     * @param dropType the drop type
     * @param dropCount the drop count
     */
    protected final void addDropCount(final PacketDropType dropType, final int dropCount) {
        dropCounter.addDrop(dropType.getPhase(), dropType.getReason(), dropCount);
    }

    /**
     * Instantiates a new abstract work handler.
     * 
     * @param name the name
     */
    protected AbstractWorkHandler(final String name) {
        handlerName = name;
        dropCounter = new DropCounterProvider();
    }

    /** The handler name. */
    private final String handlerName;

    /** The join pipe. */
    private SortPipePut<R> joinPipe;

    /** The throughput packet count. */
    private transient long throughputPacketsCount;

    /** The throughput bytes count. */
    private transient long throughputBytesCount;

    /** The drop counter. */
    private final DropCounter dropCounter;

}
