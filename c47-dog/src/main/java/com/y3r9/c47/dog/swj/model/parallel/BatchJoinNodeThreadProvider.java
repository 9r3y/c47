package com.y3r9.c47.dog.swj.model.parallel;

/**
 * The Class BatchJoinNodeThreadProvider.
 * 
 * @param <R> the generic type
 * @version 1.0
 * @see AbstractJoinNodeThreadProvider
 * @since project 3.0
 */
public final class BatchJoinNodeThreadProvider<R> extends AbstractJoinNodeThreadProvider<R> {

    @Override
    public boolean processItem() {
        return processBatchData();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BatchJoinNodeThreadProvider [").append(super.toString()).append("]");
        return builder.toString();
    }

    /**
     * Instantiates a new batch join node thread provider.
     * 
     * @param name the name
     */
    public BatchJoinNodeThreadProvider(final String name) {
        super(name);
    }
}
