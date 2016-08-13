package com.y3r9.c47.dog.swj.model.parallel.spi;

/**
 * The Interface GraphAutoParam.
 * 
 * @version 1.0
 * @see PartitionParam
 * @since project 3.0
 */
public interface GraphAutoParam extends PartitionParam {

    /**
     * Sets the partition count.
     * 
     * @param value the new partition count. <code>null</code> for the parameter is missing.
     */
    void setPartitionCount(Integer value);

    /**
     * Sets the join pipe capability.
     * 
     * @param value the new join pipe capability. <code>null</code> for the parameter is missing.
     */
    void setJoinPipeCapability(Integer value);
}
