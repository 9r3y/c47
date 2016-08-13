package com.y3r9.c47.dog.swj.model.parallel.spi;

/**
 * The Interface NameBuilder.
 * 
 * @version 1.0
 * @since project 3.0
 */
public interface NameBuilder {

    /**
     * Gets the join node name.
     * 
     * @return the join node name
     */
    String getJoinNodeName();

    /**
     * Gets the split node name.
     * 
     * @return the split node name
     */
    String getSplitNodeName();

    /**
     * Gets the partition node title.
     * 
     * @param partId the part id
     * @return the partition node title
     */
    String getPartitionNodeTitle(int partId);

    /**
     * Gets the work node name.
     * 
     * @param workId the work id
     * @return the work node name
     */
    String getWorkNodeName(int workId);

    /**
     * Gets the work handler name.
     * 
     * @param workId the work id
     * @return the work handler name
     */
    String getWorkHandlerName(int workId);
}
