package com.y3r9.c47.dog.swj.model.parallel.spi;

import java.util.List;

import cn.com.netis.dp.commons.common.Configurable;
import cn.com.netis.dp.commons.common.Node;
import com.y3r9.c47.dog.swj.model.pipeline.PipelineInput;
import com.y3r9.c47.dog.swj.model.sort.spi.SortPipeStatus;

/**
 * The Interface SplitNode.
 * 
 * <pre>
 * XML example for configuration:
 * 
 * <splitNode>
 *   <cacheSize>50000</cacheSize>
 *   <overSizeRetryCount>16</overSizeRetryCount>
 *   <overSizeBlockingNano>1</overSizeBlockingNano>
 * </splitNode>
 * 
 * </pre>
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @see PipelineInput, Node, Configurable
 * @since project 3.0
 */
public interface SplitNode<D, C, R> extends PipelineInput<D>, Node, Configurable {

    /** The default split node over size retry count. */
    int DEFAULT_SPLIT_NODE_OVER_SIZE_RETRY_COUNT = 16;

    /**
     * Gets the join pipe status.
     * 
     * @return the join pipe status
     */
    SortPipeStatus getJoinPipeStatus();

    /**
     * Sets the join pipe status.
     * 
     * @param value the new join pipe status
     */
    void setJoinPipeStatus(SortPipeStatus value);

    /**
     * Gets the dispatch token.
     * 
     * @return the dispatch token
     */
    long getDispatchToken();

    /**
     * Gets the in use token count.
     * 
     * @return the in use token count
     */
    int getInUseTokenCount();

    /**
     * Gets the cache token count.
     * 
     * @return the cache token count
     */
    int getCacheTokenCount();

    /**
     * Sets the cache token count.
     * 
     * @param value the new cache token count. <code>null</code> for this parameter is missing.
     */
    void setCacheTokenCount(Integer value);

    /**
     * Gets the span handler.
     * 
     * @return the span handler
     */
    DispatchSpanHandler<D> getSpanHandler();

    /**
     * Sets the span handler.
     * 
     * @param value the new span handler
     */
    void setSpanHandler(DispatchSpanHandler<D> value);

    /**
     * Gets the partition node manager.
     * 
     * @return the partition node manager
     */
    PartitionNodeManageable<D, C, R> getPartitionNodeManager();

    /**
     * Sets the partition node manager.
     * 
     * @param value the value
     */
    void setPartitionNodeManager(PartitionNodeManageable<D, C, R> value);

    /**
     * Gets the work nodes.
     * 
     * @return the work nodes
     */
    List<WorkNode<D, C, R>> getWorkNodes();

    /**
     * Sets the work nodes.
     * 
     * @param workNodes the work nodes
     */
    void setWorkNodes(List<WorkNode<D, C, R>> workNodes);

    /**
     * Gets the join node.
     * 
     * @return the join node
     */
    JoinNode<R> getJoinNode();

    /**
     * Sets the join node.
     * 
     * @param joinNode the new join node
     */
    void setJoinNode(JoinNode<R> joinNode);

}
