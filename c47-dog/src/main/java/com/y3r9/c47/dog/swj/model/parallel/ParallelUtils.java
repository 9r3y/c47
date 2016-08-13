package com.y3r9.c47.dog.swj.model.parallel;

import org.apache.commons.configuration.Configuration;

import com.y3r9.c47.dog.swj.config.key.ParallelKey;
import com.y3r9.c47.dog.swj.model.parallel.spi.ParallelDataType;
import com.y3r9.c47.dog.swj.model.sort.SortPipeType;

/**
 * The Class ParallelUtils.
 * 
 * @version 1.0
 * @since project 3.0
 */
public final class ParallelUtils {

    /** The Constant DEFAULT_PIPE_TYPE. */
    public static final SortPipeType DEFAULT_PIPE_TYPE = SortPipeType.unlock;

    /** The default parallel data type. */
    public static final ParallelDataType DEFAULT_PARALLEL_DATA_TYPE = ParallelDataType.batch;

    /**
     * Gets the parallel config.
     * 
     * @param config the config
     * @return the parallel config
     */
    public static Configuration getParallelConfig(final Configuration config) {
        return config == null ? null : getConfig(config, ParallelKey.parallel.name(), null);
    }

    /**
     * Gets the config.
     * 
     * @param config the config
     * @param key the key
     * @param defaultValue the default value
     * @return the config
     */
    public static Configuration getConfig(final Configuration config, final String key,
            final Configuration defaultValue) {
        if (config.containsKey(key)) {
            return (Configuration) config.getProperty(key);
        }
        return defaultValue;
    }

    /**
     * Gets the worker count.
     * 
     * @param parallelConfig the parallel config
     * @return the worker count. <code>0</code> to disable the parallel schema.
     */
    public static int getWorkerCount(final Configuration parallelConfig) {
        return parallelConfig == null ? 0 : parallelConfig
                .getInt(ParallelKey.workerCount.name(), 0);
    }

    /**
     * Gets the work node count.
     * 
     * @param workerCount the worker count
     * @return the work node count
     */
    public static int getWorkNodeCount(final int workerCount) {
        if (workerCount > 1) {
            // 1 for joinNode
            return workerCount - 1;
        }
        return workerCount == 0 ? 0 : 1;
    }

    /**
     * Checks if is partition binding.
     * 
     * @param parallelConfig the parallel config
     * @return true, if is partition binding
     */
    public static boolean isPartitionBinding(final Configuration parallelConfig) {
        return parallelConfig == null ? false : parallelConfig
                .getBoolean(ParallelKey.partBind.name(), false);
    }

    /**
     * Gets the pipe type.
     *
     * @param nodeConfig the parallel config
     * @return the pipe type
     */
    public static SortPipeType getPipeType(final Configuration nodeConfig) {
        if (nodeConfig == null || !nodeConfig.containsKey(ParallelKey.pipeType.name())) {
            return DEFAULT_PIPE_TYPE;
        }
        return SortPipeType.valueOf(nodeConfig.getString(ParallelKey.pipeType.name())
                .toLowerCase());
    }

    /**
     * Gets the pipe type.
     *
     * @param parallelConfig the parallel config
     * @param nodeKey the node key
     * @return the pipe type
     */
    public static SortPipeType getPipeType(final Configuration parallelConfig,
            final ParallelKey nodeKey) {
        return getPipeType(getNodeConfig(parallelConfig, nodeKey));
    }

    /**
     * Gets the parallel data type.
     * 
     * @param nodeConfig the node config
     * @return the parallel data type
     */
    public static ParallelDataType getParallelDataType(final Configuration nodeConfig) {
        if (nodeConfig == null || !nodeConfig.containsKey(ParallelKey.dataType.name())) {
            return DEFAULT_PARALLEL_DATA_TYPE;
        }
        return ParallelDataType.valueOf(nodeConfig.getString(ParallelKey.dataType.name())
                .toLowerCase());
    }

    /**
     * Gets the parallel data type.
     * 
     * @param parallelConfig the parallel config
     * @param nodeKey the node key
     * @return the parallel data type
     */
    public static ParallelDataType getParallelDataType(final Configuration parallelConfig,
            final ParallelKey nodeKey) {
        return getParallelDataType(getNodeConfig(parallelConfig, nodeKey));
    }

    /**
     * Gets the node configuration.
     * 
     * @param parallelConfig the parallel configuration
     * @param nodeKey the node key
     * @return the node configuration
     */
    public static Configuration getNodeConfig(final Configuration parallelConfig,
            final ParallelKey nodeKey) {
        if (parallelConfig != null && parallelConfig.containsKey(nodeKey.name())) {
            return (Configuration) parallelConfig.getProperty(nodeKey.name());
        }
        return null;
    }

    /**
     * Gets the integer configuration.
     * 
     * @param config the configuration
     * @param key the key
     * @return the integer object
     */
    public static Integer getIntegerConfig(final Configuration config, final ParallelKey key) {
        return config.getInteger(key.name(), null);
    }

    /**
     * Gets the parallel graph auto parameter.
     * 
     * @param parallelConfig the parallel config
     * @param defaultWorkerCount the default worker count
     * @return the parallel graph auto parameter
     */
    public static ParallelGraphAutoParam getGraphAutoParameter(
            final Configuration parallelConfig, final int defaultWorkerCount) {
        // safe get worker counter
        final int workerCount = parallelConfig.getInt(ParallelKey.workerCount.name(),
                defaultWorkerCount);
        // create auto parameter
        final ParallelGraphAutoParam result = new ParallelGraphAutoParameter(workerCount);
        // adjust by factor
        result.setWorkerToPartFactor(getIntegerConfig(parallelConfig,
                ParallelKey.workerToPartFactor));
        result.setPartToPipeCapabilityFactor(getIntegerConfig(parallelConfig,
                ParallelKey.partToPipeCapabilityFactor));
        // adjust directly
        result.setPartitionCount(getIntegerConfig(parallelConfig, ParallelKey.partitionCount));
        result.setJoinPipeCapability(getIntegerConfig(parallelConfig,
                ParallelKey.joinPipeCapability));
        return result;
    }

    /**
     * Gets the group parallel graph auto parameter.
     * 
     * @param parallelConfig the parallel config
     * @param defaultWorkerCount the default worker count
     * @param groupCount the group count
     * @return the group parallel graph auto parameter
     */
    public static GroupParallelGraphAutoParam getGroupGraphAutoParameter(
            final Configuration parallelConfig, final int defaultWorkerCount,
            final int groupCount) {
        // safe get worker counter
        final int workerCount = parallelConfig.getInt(ParallelKey.workerCount.name(),
                defaultWorkerCount);
        // create auto parameter
        final GroupParallelGraphAutoParam result = new GroupParallelGraphAutoParameter(workerCount,
                groupCount);
        // adjust by factor
        result.setWaysInsideGroup(getIntegerConfig(parallelConfig, ParallelKey.waysInsideGroup));
        result.setPartToPipeCapabilityFactor(getIntegerConfig(parallelConfig,
                ParallelKey.partToPipeCapabilityFactor));
        // adjust directly
        result.setPartitionCount(getIntegerConfig(parallelConfig, ParallelKey.partitionCount));
        result.setJoinPipeCapability(getIntegerConfig(parallelConfig,
                ParallelKey.joinPipeCapability));
        return result;
    }

    /**
     * Quick get ways inside group.
     * 
     * @param parallelConfig the parallel config
     * @return the ways inside group
     */
    public static int quickGetWaysInsideGroup(final Configuration parallelConfig) {
        if (parallelConfig == null) {
            return GroupParallelGraphAutoParam.DEFAULT_WAYS_INSIDE_GROUP;
        }
        return parallelConfig.getInt(ParallelKey.waysInsideGroup.toString(),
                GroupParallelGraphAutoParam.DEFAULT_WAYS_INSIDE_GROUP);
    }

    /**
     * Instantiates a new builds the utils.
     */
    private ParallelUtils() {
    }
}
