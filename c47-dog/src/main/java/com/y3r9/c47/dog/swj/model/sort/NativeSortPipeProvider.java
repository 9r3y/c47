package com.y3r9.c47.dog.swj.model.sort;

import java.util.Arrays;
import java.util.List;

import cn.com.netis.dp.commons.lang.Constants;
import com.y3r9.c47.dog.swj.value.Power2Utils;
import com.y3r9.c47.dog.swj.model.sort.spi.SortPipe;

/**
 * The Class SortPipeProvider. Native implements of sortPipe.
 * 
 * @param <V> the value type
 * @version 1.0
 * @see SortPipe
 * @since project 3.0
 */
public class NativeSortPipeProvider<V> implements SortPipe<V> {

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
        popToken = 0;
        indexMask = size - 1;
        values = new Object[size];
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
        return popToken;
    }

    @Override
    public void put(final long token, final V value) {
        /**
         * Because put procedure doesn't change popToken, so, just lockPopForRead().
         */
        doPutValue(token, value);
    }

    /**
     * Do put value.
     * 
     * @param token the token
     * @param value the value
     */
    protected final void doPutValue(final long token, final V value) {
        final int index = getIndex(token);
        values[index] = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V pop() {
        final int index = getIndex(popToken);

        final V result = (V) values[index];
        if (result != null) {
            // if pop successfully, the previous value will be set null.
            values[index] = null;
            // update popToken
            popToken++;
        }
        // if pop fail, will return null.
        return result;

    }

    @SuppressWarnings("unchecked")
    @Override
    public final void pop(final List<V> result) {
        long token = getOutToken();
        int index = getIndex(token);

        while (values[index] != null) {
            result.add((V) values[index]);
            values[index] = null;
            token++;
            index = getIndex(token);
        }

        final int size = result.size();
        if (size > 0) {
            popToken += size;
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

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("NativeSortPipeProvider [popToken=").append(popToken).append(", indexMask=")
                .append(indexMask).append(", values=").append(Arrays.toString(values)).append(']');
        return builder.toString();
    }

    /**
     * Instantiates a new abstract token sort.
     * 
     * @param size the size
     */
    public NativeSortPipeProvider(final int size) {
        setCapability(size);
    }

    /**
     * Instantiates a new token pool provider.
     */
    public NativeSortPipeProvider() {
        this(DEFAULT_CAPABILITY);
    }

    /** The pop token. */
    private transient long popToken;

    /** The index mask. */
    private transient int indexMask;

    /** The values. */
    private transient Object[] values;
}
