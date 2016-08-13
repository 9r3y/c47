package com.y3r9.c47.dog.swj.model.parallel.spi;

import cn.com.netis.dp.commons.common.statis.ParallelGraphObservable;
import org.apache.commons.configuration.Configuration;

/**
 * The Interface ParallelGraph.
 * <pre>
 * XML example for parallelConfig:
 * <parallel>
 *   <!-- 0 to disable parallel graph functionality. 1 for the sole mode, which means split, work,
 *      and join are running in a single thread. 2 for a separate work thread and a separate join
 *      thread, the join node will be attached to the previous pipeline. 3 for three threads for
 *      the split node, the work node, and the join node, in respective with. from 4 and up on,
 *      more work node will be added. -->
 *   <workerCount>8</workerCount>
 *   <!-- whether to bind the partition to the work node. -->
 *   <partBind>false</partBind>
 *   <!-- recommend config factors -->
 *   <workerToPartFactor>16</workerToPartFactor>
 *   <partToPipeCapabilityFactor>512</partToPipeCapabilityFactor>
 *   <!-- config directly
 *   <partitionCount>128</partitionCount>
 *   <joinPipeCapability>65536</joinPipeCapability>
 *   -->
 *   <!-- cacheTokenCount is an alias of /parallel/splitNode/cacheSize. -->
 *   <cacheTokenCount>50000</cacheTokenCount>
 *   <!-- can be "unlock" and "atomic". -->
 *   <pipeType>unlock</pipeType>
 *   <splitNode>
 *     <cacheSize>50000</cacheSize>
 *     <overSizeRetryCount>16</overSizeRetryCount>
 *     <overSizeBlockingNano>1</overSizeBlockingNano>
 *   </splitNode>
 *   <workNode>
 *     <idleRetryCount>8</idleRetryCount>
 *     <ideaSleepNano>1</ideaSleepNano>
 *   </workNode>
 *   <joinNode>
 *     <idleRetryCount>-1</idleRetryCount>
 *     <ideaSleepNano>1</ideaSleepNano>
 *   </joinNode>
 * </parallel>
 * </pre>
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @see PartitionParam
 * @since project 3.0
 */
public interface ParallelGraph<D, C, R> extends PartitionParam, ParallelGraphObservable {

    /**
     * Builds the graph. The <code>workerCount</code> is used to guide the build process for
     * different mode.
     * <code>0</code> attach mode, which means the parallel graph feature runs in the invoker
     * thread, a.k.a the previous pipeline, does not own an independent thread. it holds 0 thread, 1
     * combined work node. and it only works in 1 partition.
     * <code>1</code> sole mode, which means all kinds of node, including the split node, the work
     * node, and the join node, are running in the same thread, one single independent thread. it
     * holds 1 thread, and 1 combined work node. and it supports multiple partitions.
     * <code>2</code> work-join mode, which means the split node runs in attach mode, and the work
     * node and the join node run in two separated threads. it holds 2 thread and 1 combined work
     * node. and it supports multiple partitions.
     * <code>N</code> N is > 2. full mode, which means all kinds of node, including the split node,
     * the work node, and the join node, have their own thread. and there are N - 1 independent work
     * nodes in the system. it holds N thread, N - 1 independent work node. and it supports multiple
     * partitions.
     *
     * @param builder the builder
     * @param parallelConfig the parallel config
     */
    void buildGraph(ParallelBuilder<D, C, R> builder, Configuration parallelConfig);

    /**
     * Gets the split node.
     *
     * @return the split node
     */
    SplitNode<D, C, R> getSplitNode();

    /**
     * Gets the work node.
     *
     * @param index the index
     * @return the work node
     */
    WorkNode<D, C, R> getWorkNode(int index);

    /**
     * Gets the join node.
     *
     * @return the join node
     */
    JoinNode<R> getJoinNode();
}
