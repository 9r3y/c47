package com.y3r9.c47.dog.swj.model.parallel;

import com.y3r9.c47.dog.swj.model.parallel.spi.WorkHandler;

/**
 * The Class PartitionNodeProvider.
 * 
 * @param <D> the generic data type
 * @param <C> the generic context type
 * @param <R> the generic result data type
 * @version 1.1
 * @see AbstractPartitionNode
 * @since project 2.0
 */
public class NoCachePartitionNodeProvider<D, C, R> extends AbstractPartitionNode<D, C, R> {

    @Override
    public final int tryConsumePartition(final int selector, final WorkHandler<D, C, R> proc,
            final long threToken) {
        throw new UnsupportedOperationException(
                "NoCachePartitionNodeProvider<D,C,R>#tryConsumePartition()");
    }

    @Override
    public final int consumePartition(final int selector, final WorkHandler<D, C, R> proc,
            final long threToken) {
        throw new UnsupportedOperationException(
                "NoCachePartitionNodeProvider<D,C,R>#consumePartition()");
    }

    @Override
    public final void addItem(final long token, final D data) {
        throw new UnsupportedOperationException(
                "NoCachePartitionNodeProvider<D,C,R>#addItem(long,D)");
    }

    @Override
    public final boolean markEmpty() {
        throw new UnsupportedOperationException(
                "NoCachePartitionNodeProvider<D,C,R>#markEmpty()");
    }

    @Override
    public final boolean unmarkEmptyAddItem(final long token, final D data) {
        throw new UnsupportedOperationException(
                "NoCachePartitionNodeProvider<D,C,R>#unmarkEmptyAddItem(long,D)");
    }

    @Override
    public final int getCacheQueueSize() {
        return 0;
    }

    @Override
    public final long getCacheQueueHeadToken() {
        throw new UnsupportedOperationException(
                "NoCachePartitionNodeProvider<D,C,R>#getCacheQueueHeadToken()");
    }

    @Override
    public final boolean isConsuming() {
        throw new UnsupportedOperationException(
                "NoCachePartitionNodeProvider<D,C,R>#isConsuming()");
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("NoCachePartitionNodeProvider, [").append(super.toString()).append("]");
        return builder.toString();
    }

    /**
     * Instantiates a new no cache partition node provider.
     */
    public NoCachePartitionNodeProvider() {
        super();
    }

    /**
     * Instantiates a new no cache partition node provider.
     * 
     * @param id the id
     */
    public NoCachePartitionNodeProvider(final int id) {
        super(id);
    }

    /**
     * Instantiates a new no cache partition node provider.
     * 
     * @param id the id
     * @param title the title
     */
    public NoCachePartitionNodeProvider(final int id, final String title) {
        super(id, title);
    }
}
