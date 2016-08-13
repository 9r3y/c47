package com.y3r9.c47.dog.swj.model.parallext.end;

import cn.com.netis.dp.commons.common.packet.Packet;
import cn.com.netis.dp.commons.lang.NullArgumentException;
import com.y3r9.c47.dog.swj.model.parallel.spi.ParallelGraph;
import com.y3r9.c47.dog.swj.model.parallel.spi.SplitNode;
import com.y3r9.c47.dog.swj.model.pipeline.PipelineEnd;
import com.y3r9.c47.dog.swj.model.pipeline.PipelineInput;

/**
 * The Class GraphPacketPipelineEndPair.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @see PipelinePair, SortPipePut
 * @since project 3.0
 */
public class GraphPacketPipelineEndPair<D, C, R> implements PipelineEndPair<D> {

    @Override
    public final PipelineInput<D> getEndLeft() {
        return splitNode;
    }

    @Override
    public final PipelineEnd<Packet> getEndRight() {
        return endRight;
    }

    @Override
    public final int getCachePacketCount() {
        return splitNode.getInUseTokenCount();
    }

    @Override
    public int getCacheFlowCount() {
        /**
         * Will be extended.
         */
        return 0;
    }

    @Override
    public int getOtherCacheObjectCount() {
        /**
         * Will be extended.
         */
        return 0;
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
        builder.append("GraphPacketPipelineEndPair [").append("pipelineName=").append(pipelineName)
                .append(", splitNode=").append(splitNode).append(", endRight=").append(endRight)
                .append("]");
        return builder.toString();
    }

    /**
     * Instantiates a new abstract graph pipeline.
     * 
     * @param name the name
     * @param graph the graph
     * @param end the end
     */
    public GraphPacketPipelineEndPair(final String name, final ParallelGraph<D, C, R> graph,
            final PipelineEnd<Packet> end) {
        NullArgumentException.check(graph, "graph");
        pipelineName = name;
        splitNode = graph.getSplitNode();
        endRight = end;
    }

    /** The pipeline name. */
    private final String pipelineName;

    /** The left. */
    private final SplitNode<D, C, R> splitNode;

    /** The next pipeline. */
    private final PipelineEnd<Packet> endRight;
}
