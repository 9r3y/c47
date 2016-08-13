package com.y3r9.c47.dog.swj.model.parallel;

/**
 * The Class SplitNodeProvider.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.0
 * @see AbstractSplitNode
 * @since project 3.0
 */
public final class SplitNodeProvider<D, C, R> extends AbstractSplitPureNode<D, C, R> {

    @Override
    protected void processTokenData(final long token, final D data) {
        // dispatch it directly
        doDispatchData(token, data);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SplitNodeProvider [").append(super.toString()).append("]");
        return builder.toString();
    }

    /**
     * Instantiates a new split node provider.
     * 
     * @param name the name
     */
    public SplitNodeProvider(final String name) {
        super(name);
    }
}
