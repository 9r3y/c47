package com.y3r9.c47.dog.swj.model.parallext;

import cn.com.netis.dp.commons.common.statis.CacheObservable;
import cn.com.netis.dp.commons.lang.NullArgumentException;
import com.y3r9.c47.dog.swj.model.parallel.spi.ParallelGraph;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNodeManageable;
import com.y3r9.c47.dog.swj.model.parallel.spi.SplitNode;
import com.y3r9.c47.dog.swj.model.pipeline.PipelineInput;
import com.y3r9.c47.dog.swj.model.pipeline.PipelineOutput;

/**
 * The Class GraphPipelinePair.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @param <V> the value result value type
 * @version 1.0
 * @see PipelinePair, SortPipePut
 * @since project 3.0
 */
public class GraphPipelinePair<D, C extends CacheObservable, R, V> implements PipelinePair<D, V> {

    @Override
    public final PipelineInput<D> getLeft() {
        return splitNode;
    }

    @Override
    public final PipelineOutput<V> getRight() {
        return right;
    }

    @Override
    public final int getCachePacketCount() {
        return splitNode.getInUseTokenCount();
    }

    @Override
    public final int getCacheFlowCount() {
        final PartitionNodeManageable<D, C, R> partManager = splitNode.getPartitionNodeManager();
        if (partManager == null) {
            return 0;
        }
        int result = 0;
        for (int i = 0; i < partManager.getUsePartitionCount(); i++) {
            final PartitionNode<D, C, R> part = partManager.getPartitionBySelector(i);
            if (part == null || part.getContext() == null) {
                continue;
            }
            result += part.getContext().getCacheFlowCount();
        }
        return result;
    }

    @Override
    public final int getOtherCacheObjectCount() {
        final PartitionNodeManageable<D, C, R> partManager = splitNode.getPartitionNodeManager();
        if (partManager == null) {
            return 0;
        }
        int result = 0;
        for (int i = 0; i < partManager.getUsePartitionCount(); i++) {
            final PartitionNode<D, C, R> part = partManager.getPartitionBySelector(i);
            if (part == null || part.getContext() == null) {
                continue;
            }
            result += part.getContext().getOtherCacheObjectCount();
        }
        return result;
    }

    /**
     * Gets the pipeline name.
     * 
     * @return the pipeline name
     */
    public final String getPipelineName() {
        return pipelineName;
    }

    /**
     * Gets the split node.
     * 
     * @return the split node
     */
    public final SplitNode<D, C, R> getSplitNode() {
        return splitNode;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("GraphPipelinePair [").append("pipelineName=").append(pipelineName)
                .append("splitNode=").append(splitNode).append(", right=").append(right)
                .append("]");
        return builder.toString();
    }

    /**
     * Instantiates a new abstract graph pipeline.
     * 
     * @param name the name
     * @param graph the graph
     * @param next the next
     */
    public GraphPipelinePair(final String name, final ParallelGraph<D, C, R> graph,
            final PipelineOutput<V> next) {
        NullArgumentException.check(graph, "graph");
        NullArgumentException.check(next, "next");
        pipelineName = name;
        splitNode = graph.getSplitNode();
        right = next;
    }

    /** The pipeline name. */
    private final String pipelineName;

    /** The left. */
    private final SplitNode<D, C, R> splitNode;

    /** The next pipeline. */
    private final PipelineOutput<V> right;
}
