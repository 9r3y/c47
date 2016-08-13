package com.y3r9.c47.dog.swj.model.parallext;

import java.io.IOException;
import java.util.List;

import org.apache.commons.configuration.Configuration;

import com.y3r9.c47.dog.swj.model.parallel.spi.DispatchSpanHandler;
import com.y3r9.c47.dog.swj.model.parallel.spi.JoinNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNodeManageable;
import com.y3r9.c47.dog.swj.model.parallel.spi.SplitNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.WorkNode;
import com.y3r9.c47.dog.swj.model.sort.spi.SortPipeStatus;

/**
 * The Class ExportSwitchSplitNodeProvider.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @see SplitNode
 * @since project 3.0
 */
public class TransparentSplitNodeProvider<D, C, R> implements SplitNode<D, C, R> {

    @Override
    public final String getNodeName() {
        return pipelineName;
    }

    @Override
    public void dispatch(final D data) {
        /**
         * Interface from PipelineInput can be extended .
         */
        throw new UnsupportedOperationException("dispatch()");
    }

    @Override
    public int getPartitionCount() {
        /**
         * Interface from PipelineInput can be extended .
         */
        throw new UnsupportedOperationException("getPartitionCount()");
    }

    @Override
    public int getUsePartitionCount() {
        /**
         * Interface from PipelineInput can be extended .
         */
        throw new UnsupportedOperationException("getUsePartitionCount()");
    }

    @Override
    public void open() throws IOException {
        /**
         * Interface from PipelineInput can be extended .
         */
        throw new UnsupportedOperationException("open()");
    }

    @Override
    public void close() throws IOException {
        /**
         * Interface from PipelineInput can be extended .
         */
        throw new UnsupportedOperationException("close()");
    }

    @Override
    public final void updateConfiguration(final Configuration config, final int hint) {
        /**
         * Should not implements this.
         */
        throw new UnsupportedOperationException("updateConfiguration()");
    }

    @Override
    public final DispatchSpanHandler<D> getSpanHandler() {
        /**
         * Should not implements this.
         */
        throw new UnsupportedOperationException("getSpanHandler()");
    }

    @Override
    public final void setSpanHandler(final DispatchSpanHandler<D> value) {
        /**
         * Should not implements this.
         */
        throw new UnsupportedOperationException("setSpanHandler()");
    }

    @Override
    public final PartitionNodeManageable<D, C, R> getPartitionNodeManager() {
        /**
         * Should not implements this.
         */
        throw new UnsupportedOperationException("getPartitionNodeManager()");
    }

    @Override
    public final void setPartitionNodeManager(final PartitionNodeManageable<D, C, R> value) {
        /**
         * Should not implements this.
         */
        throw new UnsupportedOperationException("setPartitionNodeManager()");
    }

    @Override
    public final SortPipeStatus getJoinPipeStatus() {
        /**
         * Should not implements this.
         */
        throw new UnsupportedOperationException("getJoinPipeStatus()");
    }

    @Override
    public final void setJoinPipeStatus(final SortPipeStatus value) {
        /**
         * Should not implements this.
         */
        throw new UnsupportedOperationException("setJoinPipeStatus()");
    }

    @Override
    public final long getDispatchToken() {
        /**
         * Interface from SplitNode can be extended .
         */
        throw new UnsupportedOperationException("getDispatchToken()");
    }

    @Override
    public final int getInUseTokenCount() {
        return 0;
    }

    @Override
    public final int getCacheTokenCount() {
        return 0;
    }

    @Override
    public final void setCacheTokenCount(final Integer value) {
        /**
         * Should not implements this.
         */
        throw new UnsupportedOperationException("setMaxCacheTokenCount()");
    }

    @Override
    public final List<WorkNode<D, C, R>> getWorkNodes() {
        /**
         * Should not implements this.
         */
        throw new UnsupportedOperationException("getWorkNodes()");
    }

    @Override
    public final void setWorkNodes(final List<WorkNode<D, C, R>> workNodes) {
        /**
         * Should not implements this.
         */
        throw new UnsupportedOperationException("setWorkNodes()");
    }

    @Override
    public final JoinNode<R> getJoinNode() {
        /**
         * Should not implements this.
         */
        throw new UnsupportedOperationException("getJoinNode()");
    }

    @Override
    public final void setJoinNode(final JoinNode<R> joinNode) {
        /**
         * Should not implements this.
         */
        throw new UnsupportedOperationException("setJoinNode()");
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("pipelineName=").append(pipelineName);
        return builder.toString();
    }

    /**
     * Instantiates a new export switch provider.
     * 
     * @param name the name
     */
    public TransparentSplitNodeProvider(final String name) {
        pipelineName = name;
    }

    /** The pipeline name. */
    private final String pipelineName;
}
