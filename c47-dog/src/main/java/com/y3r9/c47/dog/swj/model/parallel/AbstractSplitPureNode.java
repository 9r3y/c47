package com.y3r9.c47.dog.swj.model.parallel;

import java.io.IOException;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.y3r9.c47.dog.swj.model.parallel.spi.JoinNode;
import com.y3r9.c47.dog.swj.model.parallel.spi.WorkNode;

/**
 * The Class AbstractSplitPureNode.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 * @see AbstractSplitNode
 * @since project 3.0
 */
abstract class AbstractSplitPureNode<D, C, R> extends AbstractSplitNode<D, C, R> {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSplitPureNode.class);

    /**
     * Do dispatch data.
     * 
     * @param token the token
     * @param data the data
     */
    protected final void doDispatchData(final long token, final D data) {
        getPartitionNodeManager().dispatchItem(token, data);
    }

    @Override
    public void open() throws IOException {
        /**
         * Can be extended.
         */

        // 1st: open join node
        if (joinNode != null) {
            joinNode.open();
        }
        // 2nd: open work nodes
        for (final WorkNode<D, C, R> workNode : workNodes) {
            if (workNode != null) {
                workNode.open();
            }
        }
        // 3rd: log started
        LOG.debug(new StringBuilder().append("SplitNode=").append(getNodeName())
                .append(" started.").toString());
    }

    @Override
    public void close() throws IOException {
        /**
         * Can be extended.
         */

        // 1st: close join node first for the terminate signal
        if (joinNode != null) {
            try {
                joinNode.close();
            } catch (Exception e) {
                LOG.debug(new StringBuilder().append("SplitNode=").append(getNodeName())
                        .append(" detects an exception  while to close the join node. exception=")
                        .append(e.toString()).toString());
            }
        }

        // 2nd: force close all work nodes
        for (final WorkNode<D, C, R> workNode : workNodes) {
            if (workNode != null) {
                try {
                    workNode.close();
                } catch (Exception e) {
                    LOG.debug(new StringBuilder()
                            .append("SplitNode=").append(getNodeName())
                            .append(" detects an exception  while to close a work node.")
                            .append(" exception=").append(e.toString()).toString());
                }
            }
        }

        // 3rd: log closed
        LOG.debug(new StringBuilder().append("SplitNode=").append(getNodeName())
                .append(" closed.").toString());
    }

    @Override
    public final List<WorkNode<D, C, R>> getWorkNodes() {
        return workNodes;
    }

    @Override
    public final void setWorkNodes(final List<WorkNode<D, C, R>> value) {
        workNodes = value;
    }

    @Override
    public final JoinNode<R> getJoinNode() {
        return joinNode;
    }

    @Override
    public final void setJoinNode(final JoinNode<R> value) {
        joinNode = value;
    }

    @Override
    public void updateConfiguration(final Configuration config, final int hint) {
        /**
         * Can be extended.
         */

        super.updateConfiguration(config, hint);
    }

    /**
     * Instantiates a new split node provider.
     * 
     * @param name the name
     */
    public AbstractSplitPureNode(final String name) {
        super(name);
    }

    /** The work nodes. DO NOT ADD to toString(). */
    private transient List<WorkNode<D, C, R>> workNodes;

    /** The join node. DO NOT ADD to toString(). */
    private transient JoinNode<R> joinNode;
}
