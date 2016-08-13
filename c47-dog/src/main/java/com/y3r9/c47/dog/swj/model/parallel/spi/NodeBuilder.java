package com.y3r9.c47.dog.swj.model.parallel.spi;

/**
 * The Interface ParallelBuilder.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @since project 3.0
 */
public interface NodeBuilder<D, C, R> {

    /**
     * Creates the split node. It should make the {@link SplitNode#getPartitioner()} ready. And it
     * is better to make the {@link SplitNode#getNodeName()} unique to identify this split node.
     * 
     * At this version, this node runs in a separate thread.
     * 
     * @param name the name
     * @return the split node
     */
    SplitNode<D, C, R> createSplitNode(String name);

    /**
     * Creates the split work join node. It should make the {@link WorkNode#getWorkHandler()} and
     * the {@link JoinNode#getJoinHandler()} ready.
     * 
     * @param name the name
     * @param separateThread the separate thread
     * @return the split work join node
     */
    SplitWorkJoinNode<D, C, R> createSplitWorkJoinNode(String name, boolean separateThread);

    /**
     * Creates the partition manager.
     * 
     * @param partBind the part bind
     * @return the partition node manageable
     */
    PartitionNodeManageable<D, C, R> createPartitionManager(boolean partBind);

    /**
     * Creates the partition. It should make the {@link PartitionNode#getPartId()} ready with
     * respect to the partId. And it is better to make the {@link PartitionNode#getContext()} ready.
     * 
     * @param partId the part id
     * @param title the title
     * @param hasCache the has cache
     * @return the partition node
     */
    PartitionNode<D, C, R> createPartition(int partId, String title, boolean hasCache);

    /**
     * Creates the work node. It should make the {@link WorkNode#getWorkHandler()} ready. And it is
     * better to make the {@link WorkNode#getNodeName()} unique to identify this work thread node.
     * 
     * At this version, this node runs in a separate thread.
     * 
     * @param workId the work id
     * @param name the name
     * @return the work node
     */
    WorkNode<D, C, R> createWorkNode(int workId, String name);

    /**
     * Creates the join node. It should make the {@link JoinNode#getJoinHandler()} ready. And it is
     * better to make the {@link JoinNode#getNodeName()} unique to identify this join thread node.
     * 
     * At this version, this node runs in a separate thread.
     * 
     * @param name the name
     * @param dataType the data type
     * @return the join node
     */
    JoinNode<R> createJoinNode(String name, ParallelDataType dataType);
}
