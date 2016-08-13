package com.y3r9.c47.dog.swj.model.parallext.end;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class AbstractNullPipelineEnd.
 * 
 * @version 1.0
 * @see NullPipelineEnd
 * @since project 3.0
 */
public abstract class AbstractNullPipelineEnd<Data> implements NullPipelineEnd<Data> {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractNullPipelineEnd.class);

    /**
     * Update count.
     * 
     * @param value the value
     */
    protected final void updatePacket(final int value) {
        throughputPacketCount += value;
    }

    protected final void updateBytes(final int value) {
        throughputBytesCount += value;
    }

    @Override
    public int getPartitionCount() {
        return 1;
    }

    @Override
    public int getUsePartitionCount() {
        return 1;
    }

    @Override
    public String getNodeName() {
        return nodeName;
    }

    @Override
    public void open() throws IOException {
        LOG.debug(new StringBuilder().append("NullPipelineEnd=").append(getNodeName())
                .append(" opened.").toString());
    }

    @Override
    public void close() throws IOException {
        LOG.debug(new StringBuilder().append("NullPipelineEnd=").append(getNodeName())
                .append(" closed.").toString());
    }

    @Override
    public long getThroughputPacketsCount() {
        return throughputPacketCount;
    }

    @Override
    public long getThroughputBytesCount() {
        return throughputBytesCount;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("nodeName=").append(nodeName)
                .append(", throughputPacketCount=").append(throughputPacketCount)
                .append(", throughputBytesCount=").append(throughputBytesCount);
        return builder.toString();
    }

    /**
     * Instantiates a new abstract null pipeline end.
     * 
     * @param name the name
     */
    protected AbstractNullPipelineEnd(final String name) {
        nodeName = name;
    }

    /** The node name. */
    private final String nodeName;

    /** The throughput packet count. */
    private long throughputPacketCount;

    /** The throughput bytes count. */
    private long throughputBytesCount;
}
