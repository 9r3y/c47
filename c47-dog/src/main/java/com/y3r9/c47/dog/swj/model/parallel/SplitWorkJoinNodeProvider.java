package com.y3r9.c47.dog.swj.model.parallel;

/**
 * The Class SplitWorkJoinNodeProvider.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @see AbstractSplitWorkJoinNode
 * @since project 3.0
 */
public class SplitWorkJoinNodeProvider<D, C, R> extends AbstractSplitWorkJoinNode<D, C, R> {

    @Override
    protected final void processTokenData(final long token, final D data) {
        // dispatch it directly
        doDispatchData(token, data);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SplitWorkJoinNodeProvider [").append(super.toString()).append("]");
        return builder.toString();
    }

    /**
     * Instantiates a new work node provider.
     * 
     * @param name the name
     */
    public SplitWorkJoinNodeProvider(final String name) {
        super(name);
    }
}
