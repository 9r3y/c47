package com.y3r9.c47.dog.swj.model.parallel;

/**
 * The Class SingleJoinNodeThreadProvider.
 * 
 * @param <R> the generic type
 * @version 1.0
 * @see AbstractJoinNodeThreadProvider
 * @since project 3.0
 */
public final class SingleJoinNodeThreadProvider<R> extends AbstractJoinNodeThreadProvider<R> {

    @Override
    public boolean processItem() {
        return processSingleData();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SingleJoinNodeThreadProvider [").append(super.toString()).append("]");
        return builder.toString();
    }

    /**
     * Instantiates a new single join node thread provider.
     * 
     * @param name the name
     */
    public SingleJoinNodeThreadProvider(final String name) {
        super(name);
    }
}
