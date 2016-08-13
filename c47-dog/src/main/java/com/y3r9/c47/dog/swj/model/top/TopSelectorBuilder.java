package com.y3r9.c47.dog.swj.model.top;

import cn.com.netis.dp.commons.lang.NegativeArgumentException;
import cn.com.netis.dp.commons.lang.NotInitializedException;
import cn.com.netis.dp.commons.lang.NullArgumentException;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * The type Top selector builder.
 *
 * @param <T> the type parameter
 * @version 1.0
 * @since v3.9
 */
public final class TopSelectorBuilder<T> {

    /**
     * Build top selector.
     *
     * @return the top selector
     */
    public TopSelector<T> build() {
        validate();
        return new TopSelectorProvider<>(maxSize, comparators);
    }

    /**
     * Validate.
     */
    private void validate() {
        if (maxSize < 0) {
            throw new NotInitializedException("Max size do not set.");
        }
    }

    /**
     * Add comparator to top selector builder.
     *
     * @param comparator the comparator
     * @return the top selector builder
     */
    public TopSelectorBuilder<T> addComparator(final Comparator<T> comparator) {
        NullArgumentException.check(comparator, "TopSelectorBuilder#comparator");
        comparators.add(comparator);
        return this;
    }

    /**
     * Sets max size.
     *
     * @param maxSize the max size
     */
    public TopSelectorBuilder<T> setMaxSize(final int maxSize) {
        NegativeArgumentException.check(maxSize, "TopSelectorBuilder#maxSize");
        this.maxSize = maxSize;
        return this;
    }

    /**
     * Instantiates a new Top selector builder.
     */
    public TopSelectorBuilder() {
    }

    /**
     * Instantiates a new Top selector builder.
     *
     * @param maxSize the max size of each comparator
     */
    public TopSelectorBuilder(final int maxSize) {
        this();
        setMaxSize(maxSize);
    }

    /** The Max size. */
    private int maxSize;

    /** The Comparators. */
    private List<Comparator<T>> comparators;

    {
        comparators = new LinkedList<>();
        maxSize = -1;
    }
}
