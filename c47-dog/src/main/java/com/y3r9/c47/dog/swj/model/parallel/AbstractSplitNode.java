package com.y3r9.c47.dog.swj.model.parallel;

import cn.com.netis.dp.commons.common.ConfigHint;
import cn.com.netis.dp.commons.lang.Constants;
import cn.com.netis.dp.commons.lang.NegativeArgumentException;
import cn.com.netis.dp.commons.lang.NullArgumentException;
import com.y3r9.c47.dog.swj.model.parallel.spi.*;
import com.y3r9.c47.dog.swj.model.sort.spi.SortPipeStatus;
import com.y3r9.c47.dog.swj.polling.LoopSleepOverSizeStrategy;
import com.y3r9.c47.dog.swj.polling.PollingFactory;
import com.y3r9.c47.dog.swj.polling.spi.OverSizeStrategy;
import com.y3r9.c47.dog.swj.polling.spi.SizeProc;
import org.apache.commons.configuration.Configuration;

/**
 * The Class AbstractSplitNode.
 * Internal package only.
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 * @see SplitNode, DispatchProc, SizeProc
 * @since project 3.0
 */
abstract class AbstractSplitNode<D, C, R> implements SplitNode<D, C, R>, DispatchProc<D>, SizeProc {

    @Override
    public final int size() {
        // use graph cache token
        return getInUseTokenCount();
    }

    @Override
    public final void dispatch(final D data) {
        if (spanHandler != null) {
            spanHandler.spanData(data, this, getUsePartitionCount());
        }

        dispatchData(data);
    }

    @Override
    public final void dispatchData(final D data) {
        // wait for cache size first
        overSizeStrategy.waitForOverflow(this);

        // dispatch it directly or put into queue, dispatch token start with 0.
        processTokenData(dispatchToken, data);

        // increment the token
        dispatchToken++;
    }

    /**
     * Process token data.
     *
     * @param token the token
     * @param data the data
     */
    protected abstract void processTokenData(long token, D data);

    /**
     * Select partition.
     *
     * @param token the token
     * @param data the data
     * @return the partition node
     */
    protected final PartitionNode<D, C, R> selectPartition(final long token, final D data) {
        final int selector = partitionNodeManager.getPartSelector(token, data);
        final PartitionNode<D, C, R> part = partitionNodeManager.getPartitionBySelector(selector);
        /**
         * Because data is controlled logically, so the selector from the partitioner with respect
         * to the data should always matches a valid partition. So, the partition should never be
         * NULL, and no data will be dropped.
         */
        return part;
    }

    @Override
    public final String getNodeName() {
        return nodeName;
    }

    @Override
    public final DispatchSpanHandler<D> getSpanHandler() {
        return spanHandler;
    }

    @Override
    public final void setSpanHandler(final DispatchSpanHandler<D> value) {
        spanHandler = value;
    }

    @Override
    public final PartitionNodeManageable<D, C, R> getPartitionNodeManager() {
        return partitionNodeManager;
    }

    @Override
    public final void setPartitionNodeManager(final PartitionNodeManageable<D, C, R> value) {
        partitionNodeManager = value;
    }

    @Override
    public final SortPipeStatus getJoinPipeStatus() {
        return joinPipe;
    }

    @Override
    public final void setJoinPipeStatus(final SortPipeStatus value) {
        joinPipe = value;
    }

    @Override
    public final long getDispatchToken() {
        return dispatchToken;
    }

    @Override
    public final int getInUseTokenCount() {
        return (int) (dispatchToken - joinPipe.getOutToken());
    }

    @Override
    public final int getCacheTokenCount() {
        return overSizeStrategy.getCacheSize();
    }

    @Override
    public final void setCacheTokenCount(final Integer value) {
        if (value != null) {
            NegativeArgumentException.check(value, "cacheTokenCount");
            overSizeStrategy.setCacheSize(value);
        }
    }

    @Override
    public final int getPartitionCount() {
        return partitionNodeManager == null ? 0 : partitionNodeManager.getPartitionCount();
    }

    @Override
    public final int getUsePartitionCount() {
        return partitionNodeManager == null ? 0 : partitionNodeManager.getUsePartitionCount();
    }

    /**
     * Gets the over size strategy.
     *
     * @return the over size strategy
     */
    public final OverSizeStrategy getOverSizeStrategy() {
        return overSizeStrategy;
    }

    /**
     * Sets the over size strategy.
     *
     * @param value the new over size strategy
     */
    public void setOverSizeStrategy(final OverSizeStrategy value) {
        NullArgumentException.check(value, "overSizeBlocking");
        overSizeStrategy = value;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("nodeName=").append(nodeName).append(", dispatchToken=")
                .append(dispatchToken).append(", cacheTokenCount=").append(getInUseTokenCount())
                .append(", resultPipeOutToken()=").append(joinPipe.getOutToken())
                .append(", partitionCount=").append(getPartitionCount())
                .append(", overSizeStrategy=").append(overSizeStrategy);
        return builder.toString();
    }

    @Override
    public void updateConfiguration(final Configuration config, final int hint) {
        /**
         * Can be extended.
         */
        if (config == null) {
            return;
        }

        if (ConfigHint.NATIVE_FILE == hint || ConfigHint.CLI_OVERRIDE == hint) {
            setOverSizeStrategy(PollingFactory.updateOverSizeStrategy(overSizeStrategy, config,
                    hint));
        }
    }

    /**
     * Instantiates a new split node provider.
     *
     * @param name the name
     */
    public AbstractSplitNode(final String name) {
        nodeName = name;
        overSizeStrategy = new LoopSleepOverSizeStrategy(DEFAULT_SPLIT_NODE_OVER_SIZE_RETRY_COUNT);
        setCacheTokenCount(Constants.DEFAULT_CACHE_TOKEN_COUNT);
    }

    /** The node name. */
    private final String nodeName;

    /** The token. */
    private long dispatchToken;

    /** The over size strategy. */
    private OverSizeStrategy overSizeStrategy;

    /** The span handler. */
    private DispatchSpanHandler<D> spanHandler;

    /** The partition node manager. */
    private PartitionNodeManageable<D, C, R> partitionNodeManager;

    /** The join pipe. DO NOT ADD to toString(). */
    private transient SortPipeStatus joinPipe;
}
