package com.y3r9.c47.dog.swj.model.sort;

import cn.com.netis.dp.commons.lang.Constants;
import com.y3r9.c47.dog.swj.model.sort.spi.SortPipe;
import com.y3r9.c47.dog.swj.value.Power2Utils;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * The Class SortPipeProvider.
 *
 * @param <V> the value type
 * @version 1.0
 * @see SortPipe
 * @since project 3.0
 */
public class RefArraySortPipeProvider<V> implements SortPipe<V> {

    @Override
    public final void setCapability(final int value) {
        reset(Power2Utils.power2Ceil(value, Constants.MIN_PIPE_CAPABILITY,
                Constants.MAX_PIPE_CAPABILITY));
    }

    /**
     * Reset.
     *
     * @param size the size
     */
    private void reset(final int size) {
        popToken.getAndSet(0);
        indexMask = size - 1;
        values = new AtomicReferenceArray<V>(size);
    }

    @Override
    public final int capability() {
        return indexMask + 1;
    }

    @Override
    public final long getInTokenThre() {
        return getOutToken() + capability();
    }

    @Override
    public final long getOutToken() {
        return popToken.get();
    }

    @Override
    public void put(final long token, final V value) {
        /**
         * Can be extended.
         */
        // put directly without checking.
        // this operation will always be successful.
        doPutValue(token, value);
    }

    /**
     * Do put value. token MUST start with 0.
     *
     * @param token the token
     * @param value the value
     */
    protected final void doPutValue(final long token, final V value) {
        final int index = getIndex(token);
        values.getAndSet(index, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final V pop() {
        final int index = getIndex(getOutToken());
        final V result = values.getAndSet(index, null);
        if (result != null) {
            // if pop successfully, the previous value will be set null.
            popToken.incrementAndGet();
        }
        // if pop fail, will return null.
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void pop(final List<V> result) {
        long token = getOutToken();
        int index = getIndex(token);

        while (true) {
            final V value = values.getAndSet(index, null);
            if (value != null) {
                result.add(value);
                token++;
                index = getIndex(token);
            } else {
                break;
            }
        }

        final int size = result.size();
        if (size > 0) {
            popToken.addAndGet(size);
        }
    }

    /**
     * Gets the index.
     *
     * @param token the token
     * @return the index
     */
    public final int getIndex(final long token) {
        return (int) token & indexMask;
    }

    /**
     * Instantiates a new abstract token sort.
     *
     * @param size the size
     */
    public RefArraySortPipeProvider(final int size) {
        setCapability(size);
    }

    /**
     * Instantiates a new token pool provider.
     */
    public RefArraySortPipeProvider() {
        this(DEFAULT_CAPABILITY);
    }

    /** The pop token. */
    private final transient AtomicLong popToken;

    /** The values. */
    private transient AtomicReferenceArray<V> values;

    /** The index mask. */
    private transient int indexMask;

    {
        popToken = new AtomicLong();
    }

}
