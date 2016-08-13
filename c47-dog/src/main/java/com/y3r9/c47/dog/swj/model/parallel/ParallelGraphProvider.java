package com.y3r9.c47.dog.swj.model.parallel;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.com.netis.dp.commons.common.ConfigHint;
import cn.com.netis.dp.commons.common.statis.JoinNodeObservable;
import cn.com.netis.dp.commons.common.statis.WorkNodeObservable;
import com.y3r9.c47.dog.swj.config.key.ParallelKey;
import com.y3r9.c47.dog.swj.config.key.PollingKey;
import com.y3r9.c47.dog.swj.model.parallel.spi.HandlerBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.JoinNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.NodeBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.ParallelBuilder;
import com.y3r9.c47.dog.swj.model.parallel.spi.ParallelDataType;
import com.y3r9.c47.dog.swj.model.parallel.spi.ParallelGraph;
import com.y3r9.c47.dog.swj.model.parallel.spi.ParallelParam;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNodeManageable;
import com.y3r9.c47.dog.swj.model.parallel.spi.SplitNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.SplitWorkJoinNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.WorkNode;
import com.y3r9.c47.dog.swj.model.sort.AtomicSortPipeProvider;
import com.y3r9.c47.dog.swj.model.sort.NativeSortPipeProvider;
import com.y3r9.c47.dog.swj.model.sort.RefArraySortPipeProvider;
import com.y3r9.c47.dog.swj.model.sort.SortPipeType;
import com.y3r9.c47.dog.swj.model.sort.spi.SortPipe;

/**
 * The Class ParallelGraphProvider.
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 * @see ParallelGraph
 * @since v3.8 updates 1.1
 */
public class ParallelGraphProvider<D, C, R>
        implements ParallelGraph<D, C, R>, NodeBuilder<D, C, R> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SplitNode<D, C, R> createSplitNode(final String name) {
        /**
         * Can be extended.
         */
        return new SplitNodeProvider<>(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SplitWorkJoinNode<D, C, R> createSplitWorkJoinNode(final String name,
            final boolean separateThread) {
        /**
         * Can be extended.
         */
        return separateThread ? new SplitWorkJoinNodeThreadProvider<D, C, R>(name)
                : new SplitWorkJoinNodeProvider<D, C, R>(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PartitionNodeManageable<D, C, R> createPartitionManager(final boolean partBind) {
        /**
         * Can be extended.
         */
        return partBind ? new ModPartitionNodeManager<D, C, R>()
                : new MaskPartitionNodeManager<D, C, R>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PartitionNode<D, C, R> createPartition(final int partId, final String title,
            final boolean hasCache) {
        /**
         * Can be extended.
         */
        return hasCache ? new PartitionNodeProvider<D, C, R>(partId, title)
                : new NoCachePartitionNodeProvider<D, C, R>(partId, title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkNode<D, C, R> createWorkNode(final int workId, final String name) {
        /**
         * Can be extended.
         */
        return new WorkNodeThreadProvider<>(workId, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JoinNode<R> createJoinNode(final String name, final ParallelDataType dataType) {
        /**
         * Can be extended.
         */
        return dataType == ParallelDataType.single ? new SingleJoinNodeThreadProvider<R>(name)
                : new BatchJoinNodeThreadProvider<R>(name);
    }

    @Override
    public final int getWorkerCount() {
        return workNodes.size();
    }

    @Override
    public final int getPartitionCount() {
        return splitNode.getPartitionCount();
    }

    @Override
    public int getUsePartitionCount() {
        /**
         * Can be extended.
         */
        return getPartitionCount();
    }

    @Override
    public final int getJoinPipeCapability() {
        return joinNode.getJoinPipe().capability();
    }

    @Override
    public void buildGraph(final ParallelBuilder<D, C, R> builder,
            final Configuration parallelConfig) {
        /**
         * Can be extended.
         */

        graphBuilder = builder;
        nodeBuilder = graphBuilder.getNodeBuilder() == null ? this : graphBuilder.getNodeBuilder();
        handlerBuilder = graphBuilder.getHandlerBuilder();

        // update parameter
        if (parallelConfig != null) {
            updateParameter(parallelConfig, builder);
        }
        graphConfig = parallelConfig;

        // use worker count to guide the builder
        final int workerCount = builder.getWorkerCount();

        /**
         * workerCount is used to guide the build process for different mode.
         *
         * <code>0</code> attach mode, which means the parallel graph feature runs in the invoker
         * thread, a.k.a the previous pipeline, does not own an independent thread. it holds 0
         * thread, 1 combined work node. and it only works in 1 partition.
         *
         * <code>1</code> sole mode, which means all kinds of node, including the split node, the
         * work node, and the join node, are running in the same thread, one single independent
         * thread. it holds 1 thread, and 1 combined work node. and it supports multiple partitions.
         *
         * <code>2</code> work-join mode, which means the split node runs in attach mode, and the
         * work node and the join node run in two separated threads. it holds 2 thread and 1
         * combined work node. and it supports multiple partitions.
         *
         * <code>N</code> N is > 2. full mode, which means all kinds of node, including the split
         * node, the work node, and the join node, have their own thread. and there are N - 1
         * independent work nodes in the system. it holds N thread, N - 1 independent work node. and
         * it supports multiple partitions.
         */

        if (workerCount == 0) {
            /**
             * attach mode.
             */

            // do not use separate thread
            initializeSplitWorkJoinNode(false);

        } else if (workerCount == 1) {
            /**
             * sole mode.
             */

            // must run in a separate thread.
            initializeSplitWorkJoinNode(true);

        } else {
            /**
             * work-join mode and full mode
             */

            final int workNodeCount = ParallelUtils.getWorkNodeCount(workerCount);
            final boolean partBind = ParallelUtils.isPartitionBinding(parallelConfig);

            // get partition count
            final int partCount = partBind ? workNodeCount : graphBuilder.getPartitionCount();
            final int usePartCount = partBind ? workNodeCount : graphBuilder.getUsePartitionCount();

            // initialize split node without thread
            initializeSplitNode(partBind, partCount, usePartCount);

            // initialize work node threads
            initializeWorkNodeThreads(workNodeCount, partCount);

            // initialize join node thread
            initializeJoinNodeThread();

            // link nodes
            linkForSeparateSplitNode(partBind);
        }

    }

    /**
     * Update parameter.
     *
     * @param parallelConfig the parallel configuration
     * @param parallelParam the default parameter
     */
    protected void updateParameter(final Configuration parallelConfig,
            final ParallelParam parallelParam) {
        /**
         * Will be extended.
         */
        // create auto parameter
        final ParallelGraphAutoParam p = ParallelUtils.getGraphAutoParameter(
                parallelConfig, parallelParam.getWorkerCount());
        // apply the parameter
        parallelParam.updateParam(p);
    }

    /**
     * Builds the sole mode.
     *
     * @param separateThread the separate thread
     */
    protected final void initializeSplitWorkJoinNode(final boolean separateThread) {
        final SplitWorkJoinNode<D, C, R> splitWorkJoinNode = nodeBuilder
                .createSplitWorkJoinNode(graphBuilder.getSplitNodeName(), separateThread);
        if (splitWorkJoinNode == null) {
            throw new IllegalArgumentException("splitWorkJoinNode is EMPTY.");
        }

        /**
         * initialize for split node part.
         */

        splitNode = splitWorkJoinNode;

        // check and update it
        checkUpdateSplitNode();

        // initialize partition nodes to split without cache
        initializePartitionNodesToSplitNode(false, graphBuilder.getPartitionCount(),
                graphBuilder.getUsePartitionCount(), false);

        // update configuration from the work node for idle related.
        splitNode.updateConfiguration(getNodeConfig(ParallelKey.workNode), ConfigHint.NATIVE_FILE);

        /**
         * initialize work handler to split. do not load <workNode> configuration.
         */

        if (splitWorkJoinNode.getWorkHandler() == null) {
            if (handlerBuilder != null) {
                splitWorkJoinNode.setWorkHandler(handlerBuilder.getWorkHandler(0));
            }
            if (splitWorkJoinNode.getWorkHandler() == null) {
                throw new IllegalArgumentException("getWorkHandler() of workNode is EMPTY.");
            }
        }

        /**
         * initialize join handler to split. do not load <joinNode> configuration.
         */

        joinNode = splitWorkJoinNode;
        if (joinNode.getJoinHandler() == null) {
            if (handlerBuilder != null) {
                joinNode.setJoinHandler(handlerBuilder.getJoinHandler());
            }
            if (joinNode.getJoinHandler() == null) {
                throw new IllegalArgumentException("getJoinHandler() of joinNode is EMPTY.");
            }
        }
        // no pipeType required
        // no join pipe required
    }

    /**
     * Initialize split node.
     *
     * @param partBind the part bind
     * @param partCount the part count
     * @param usePartCount the use part count
     */
    protected final void initializeSplitNode(final boolean partBind, final int partCount,
            final int usePartCount) {
        // create it normally.
        splitNode = nodeBuilder.createSplitNode(graphBuilder.getSplitNodeName());

        // check and update it
        checkUpdateSplitNode();

        // initialize partition nodes to split with cache.
        initializePartitionNodesToSplitNode(partBind, partCount, usePartCount, true);
    }

    /**
     * Check update split node.
     */
    private void checkUpdateSplitNode() {
        if (splitNode == null) {
            throw new IllegalArgumentException("splitNode is EMPTY.");
        }
        if (splitNode.getSpanHandler() == null) {
            // can be null
            splitNode.setSpanHandler(handlerBuilder.getSpanHandler());
        }

        // update the configuration for over size related.
        splitNode.updateConfiguration(getNodeConfig(ParallelKey.splitNode), ConfigHint.NATIVE_FILE);

        // update max cache token count after update configuration
        splitNode.setCacheTokenCount(getIntegerConfig(ParallelKey.cacheTokenCount));
    }

    /**
     * Initialize partition nodes to split node.
     *
     * @param partBind the part bind
     * @param partCount the part count
     * @param usePartCount the use part count
     * @param partHasCache the part has cache
     */
    private void initializePartitionNodesToSplitNode(final boolean partBind, final int partCount,
            final int usePartCount, final boolean partHasCache) {
        // check partition manager
        final PartitionNodeManageable<D, C, R> manager = nodeBuilder
                .createPartitionManager(partBind);
        final Configuration partitionConfig = getNodeConfig(ParallelKey.partitionNode);
        manager.updateConfiguration(partitionConfig, ConfigHint.NATIVE_FILE);
        if (manager.getPartitioner() == null) {
            if (handlerBuilder != null) {
                final ParallelDataType dataType = ParallelUtils.getParallelDataType(graphConfig,
                        ParallelKey.splitNode);
                manager.setPartitioner(handlerBuilder.getPartitioner(dataType));
            }
            if (manager.getPartitioner() == null) {
                throw new IllegalArgumentException("getPartitioner() of splitNode is EMPTY.");
            }
        }
        splitNode.setPartitionNodeManager(manager);

        // set count
        manager.setPartitionCount(partCount);
        manager.setUsePartitionCount(usePartCount);

        // initialize partition nodes with or without cache
        for (int i = 0; i < usePartCount; i++) {
            final PartitionNode<D, C, R> partNode = nodeBuilder.createPartition(i,
                    graphBuilder.getPartitionNodeTitle(i), partHasCache);
            if (partNode == null) {
                throw new IllegalArgumentException("partitionNode is EMPTY.");
            }
            partNode.updateConfiguration(partitionConfig, ConfigHint.NATIVE_FILE);
            if (partNode.getContext() == null) {
                if (handlerBuilder != null) {
                    partNode.setContext(handlerBuilder.getPartitionContextManager(i, usePartCount));
                }
            }
            // put into partition manager
            manager.setPartition(i, partNode);
        }
    }

    /**
     * Initialize work node threads.
     *
     * @param workThreadCount the work thread count
     * @param partCount the partition count
     */
    protected final void initializeWorkNodeThreads(final int workThreadCount, final int partCount) {
        final Configuration workConfig = getNodeConfig(ParallelKey.workNode);
        workNodes = new ArrayList<>(workThreadCount);

        final boolean concurrentBuilderEnabled = (workConfig != null) && workConfig.getBoolean(
                PollingKey.concurrentBuilderEnabled.name(), false);
        final NodeFactory<D, C, R> factory = concurrentBuilderEnabled ?
                new ConcurrentlyNodeBuilder<>() : new SimpleNodeBuilder<>();
        final WorkNode<D, C, R>[] workNodeArray = factory.createWorkNodes(workThreadCount,
                nodeBuilder, graphBuilder, handlerBuilder);

        final boolean scatterSelector = workConfig != null && workConfig.getBoolean(
                PollingKey.scatterSelector.name(), false);
        final boolean swingSelector = workConfig != null && workConfig.getBoolean(
                PollingKey.swingSelector.name(), false);
        final int avg = partCount / workThreadCount;
        final int half = workThreadCount / 2;
        for (int i = 0; i < workThreadCount; i++) {
            // must runs in a separate thread.
            final WorkNode<D, C, R> workNode = workNodeArray[i];

            if (scatterSelector) {
                // Let workNodes start select at average scattered point of partitions.
                workNode.setSelector(avg * i);
            }
            if (swingSelector && i >= half) {
                // Let the second half of workNodes reverse select direction.
                workNode.reverseSelectorDirection();
            }

            // update configuration from the work node for idle related.
            workNode.updateConfiguration(workConfig, ConfigHint.NATIVE_FILE);

            // put into work nodes
            workNodes.add(i, workNode);
        }
    }

    /**
     * Initialize join node thread.
     */
    protected final void initializeJoinNodeThread() {
        final ParallelDataType dataType = ParallelUtils.getParallelDataType(graphConfig,
                ParallelKey.joinNode);
        joinNode = nodeBuilder.createJoinNode(graphBuilder.getJoinNodeName(), dataType);
        if (joinNode == null) {
            throw new IllegalArgumentException("joinNode is EMPTY.");
        }
        if (joinNode.getJoinHandler() == null) {
            if (handlerBuilder != null) {
                joinNode.setJoinHandler(handlerBuilder.getJoinHandler());
            }
            if (joinNode.getJoinHandler() == null) {
                throw new IllegalArgumentException("getJoinHandler() of joinNode is EMPTY.");
            }
        }
        // set join pipe with respect to the pipeType.
        final SortPipeType pipeType = ParallelUtils.getPipeType(graphConfig, ParallelKey.joinNode);
        if (pipeType == SortPipeType.atomic) {
            joinNode.setJoinPipe(new AtomicSortPipeProvider<R>());
        } else if (pipeType == SortPipeType.refarray) {
            joinNode.setJoinPipe(new RefArraySortPipeProvider<R>());
        } else {
            joinNode.setJoinPipe(new NativeSortPipeProvider<R>());
        }
        if (joinNode.getJoinPipe() == null) {
            throw new IllegalArgumentException("getJoinPipe() of joinNode is EMPTY.");
        }
        joinNode.setJoinPipeCapability(graphBuilder.getJoinPipeCapability());
        joinNode.updateConfiguration(getNodeConfig(ParallelKey.joinNode), ConfigHint.NATIVE_FILE);
    }

    /**
     * Link for separate split node.
     *
     * @param partBind the part bind
     */
    protected final void linkForSeparateSplitNode(final boolean partBind) {
        // link join pipe to split node.
        SortPipe<R> resultPipe = joinNode.getJoinPipe();
        if (resultPipe == null) {
            throw new IllegalArgumentException("getJoinPipe() of joinNode is EMPTY.");
        }
        splitNode.setJoinPipeStatus(resultPipe);

        // workNodes and joinNode are visible to splitNode.
        splitNode.setWorkNodes(workNodes);
        splitNode.setJoinNode(joinNode);

        final PartitionNodeManageable<D, C, R> partManager = splitNode.getPartitionNodeManager();
        if (partManager == null) {
            throw new IllegalArgumentException("getPartitionNodeManager() of splitNode is EMPTY.");
        }

        // link split node and join node to worker
        int index = 0;
        for (WorkNode<D, C, R> workNode : workNodes) {
            if (partBind) {
                // link to the partition directly
                workNode.setPartScheduler(new BindPartitionScheduler<>(partManager, index));
            } else {
                // link to partition manager, let it select the partition
                workNode.setPartScheduler(partManager);
            }
            // link join node
            workNode.setJoinPipePut(resultPipe);
            index++;
        }
    }

    /**
     * Gets the node config.
     *
     * @param nodeKey the node key
     * @return the node config
     */
    private Configuration getNodeConfig(final ParallelKey nodeKey) {
        return ParallelUtils.getNodeConfig(graphConfig, nodeKey);
    }

    /**
     * Gets the integer config.
     *
     * @param key the key
     * @return the integer config
     */
    private Integer getIntegerConfig(final ParallelKey key) {
        return ParallelUtils.getIntegerConfig(graphConfig, key);
    }

    @Override
    public final SplitNode<D, C, R> getSplitNode() {
        return splitNode;
    }

    @Override
    public final WorkNode<D, C, R> getWorkNode(final int index) {
        return workNodes.get(index);
    }

    @Override
    public final JoinNode<R> getJoinNode() {
        return joinNode;
    }

    @Override
    public String getGraphName() {
        return graphBuilder.getBuilderName();
    }

    @Override
    public int[] getPartitionProfile() {
        /**
         * @since 1.1 only fetch getUsePartitionCount() for taking groupPartitioner into account.
         */
        final int[] profile = new int[splitNode.getPartitionNodeManager().getUsePartitionCount()];
        for (int i = 0; i < profile.length; i++) {
            profile[i] = splitNode.getPartitionNodeManager().getPartitionByIndex(i)
                    .getCacheQueueSize();
        }
        return profile;
    }

    @Override
    public List<WorkNodeObservable> getWorkNodeObservable() {
        return workNodes == null ? ListUtils.EMPTY_LIST : workNodes.stream()
                .collect(Collectors.toList());
    }

    @Override
    public JoinNodeObservable getJoinNodeObservable() {
        return joinNode;
    }

    @Override
    public long getDispatchTokenPosition() {
        return splitNode.getDispatchToken();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ParallelGraphProvider [splitNode=").append(splitNode)
                .append(", joinNode=").append(joinNode).append(", getWorkCount()=")
                .append(workNodes == null ? 0 : getWorkerCount())
                .append(", getParitionCount()=")
                .append(splitNode == null ? 0 : getPartitionCount()).append("]");
        return builder.toString();
    }

    /** The graph builder. */
    private transient ParallelBuilder<D, C, R> graphBuilder;

    /** The node builder. */
    private transient NodeBuilder<D, C, R> nodeBuilder;

    /** The handler builder. */
    private transient HandlerBuilder<D, C, R> handlerBuilder;

    /** The graph config. */
    private transient Configuration graphConfig;

    /** The split node. */
    private SplitNode<D, C, R> splitNode;

    /** The work nodes. */
    private ArrayList<WorkNode<D, C, R>> workNodes;

    /** The join node. */
    private JoinNode<R> joinNode;
}
