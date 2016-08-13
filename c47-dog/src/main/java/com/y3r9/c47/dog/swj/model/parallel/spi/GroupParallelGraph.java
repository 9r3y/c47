package com.y3r9.c47.dog.swj.model.parallel.spi;

/**
 * The Interface ParallelGraph.
 * 
 * <pre>
 * XML example for parallelConfig:
 * 
 * <parallel>
 *   <!-- 0 to disable parallel graph functionality. 1 for the sole mode, which means split, work,
 *      and join are running in a single thread. 2 for a separate work thread and a separate join 
 *      thread, the join node will be attached to the previous pipeline. 3 for three threads for 
 *      the split node, the work node, and the join node, in respective with. from 4 and up on,
 *      more work node will be added. -->
 *   <workerCount>8</workerCount>
 *      
 *   <!-- whether to bind the partition to the work node. -->
 *   <partBind>false</partBind>
 *   
 *   <!-- recommend config factors -->
 *   <waysInsideGroup>4</waysInsideGroup>
 *   <partToPipeCapabilityFactor>512</partToPipeCapabilityFactor>
 *   
 *   <!-- config directly
 *   <partitionCount>128</partitionCount>
 *   <joinPipeCapability>65536</joinPipeCapability>
 *   -->
 * 
 *   <!-- cacheTokenCount is an alias of /parallel/splitNode/cacheSize. -->
 *   <cacheTokenCount>50000</cacheTokenCount>
 * 
 *   <!-- can be "unlock" and "atomic". -->
 *   <pipeType>unlock</pipeType>
 * 
 *   <splitNode>
 *     <cacheSize>50000</cacheSize>
 *     <overSizeRetryCount>16</overSizeRetryCount>
 *     <overSizeBlockingNano>1</overSizeBlockingNano>
 *   </splitNode>
 * 
 *   <workNode>
 *     <idleRetryCount>8</idleRetryCount>
 *     <ideaSleepNano>1</ideaSleepNano>
 *   </workNode>
 * 
 *   <joinNode>
 *     <idleRetryCount>-1</idleRetryCount>
 *     <ideaSleepNano>1</ideaSleepNano>
 *   </joinNode>
 * </parallel>
 * 
 * </pre>
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @see ParallelGraph, GroupPartitionParam
 * @since project 3.0
 */
public interface GroupParallelGraph<D, C, R> extends ParallelGraph<D, C, R>, GroupPartitionParam {

    /**
     * Sets the group count.
     * 
     * @param value the new group count
     */
    void setGroupCount(int value);
}
