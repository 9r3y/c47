package com.y3r9.c47.dog.swj.model.parallel;

import org.apache.commons.configuration.Configuration;

import com.y3r9.c47.dog.swj.model.parallel.spi.PartitionNode;

/**
 * The Class AbstractPartitionNode.
 *
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @see PartitionNode
 * @since project 2.0
 */
abstract class AbstractPartitionNode<D, C, R> implements PartitionNode<D, C, R> {

    @Override
    public final int getPartId() {
        return partId;
    }

    @Override
    public final void setPartId(final int value) {
        partId = value;
    }

    @Override
    public final String getPartitionName() {
        return partitionName;
    }

    @Override
    public final C getContext() {
        return context;
    }

    @Override
    public final void setContext(final C value) {
        context = value;
    }

    @Override
    public void updateConfiguration(final Configuration config, final int hint) {
        /**
         * Can be extended.
         */
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("partitionName=").append(partitionName).append(", partId=").append(partId)
                .append(", context=").append(context).append(", cacheQueue=");
        return builder.toString();
    }

    /**
     * Instantiates a new partition node provider.
     */
    public AbstractPartitionNode() {
        this(0, "PART");
    }

    /**
     * Instantiates a new partition node provider.
     *
     * @param id the id
     */
    public AbstractPartitionNode(final int id) {
        this(id, String.format("PART-%d", id));
    }

    /**
     * Instantiates a new partition node provider.
     *
     * @param id the id
     * @param title the title
     */
    public AbstractPartitionNode(final int id, final String title) {
        super();
        setPartId(id);
        partitionName = title;
    }

    /** The handler name. */
    private final String partitionName;

    /** The part id. */
    private int partId;

    /** The context. */
    private C context;
}
