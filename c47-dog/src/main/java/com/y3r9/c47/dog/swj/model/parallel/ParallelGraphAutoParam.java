package com.y3r9.c47.dog.swj.model.parallel;

import com.y3r9.c47.dog.swj.model.parallel.spi.GraphAutoParam;

/**
 * The Interface ParallelGraphAutoParam.
 * 
 * @version 1.0
 * @see GraphAutoParam
 * @since project 3.0
 */
public interface ParallelGraphAutoParam extends GraphAutoParam {

    /** The Constant DEFAULT_WORKER_TO_PART_FACTOR. */
    int DEFAULT_WORKER_TO_PART_FACTOR = 16;

    /** The Constant DEFAULT_PART_TO_PIPE_CAPABILITY. */
    int DEFAULT_PART_TO_PIPE_CAPABILITY = 128;

    /**
     * Inits the auto parameter.
     * 
     * @param workerCount the worker count
     */
    void init(int workerCount);

    /**
     * Gets the worker to part factor.
     * 
     * @return the worker to part factor
     */
    int getWorkerToPartFactor();

    /**
     * Sets the worker to part factor.
     * 
     * @param value the new worker to part factor. <code>null</code> for the parameter is missing.
     */
    void setWorkerToPartFactor(Integer value);

    /**
     * Gets the part to pipe capability factor.
     * 
     * @return the part to pipe capability factor
     */
    int getPartToPipeCapabilityFactor();

    /**
     * Sets the part to pipe capability factor.
     * 
     * @param value the new part to pipe capability factor. <code>null</code> for the parameter is
     *            missing.
     */
    void setPartToPipeCapabilityFactor(Integer value);
}
