package com.y3r9.c47.dog.swj.model.top;

import java.util.Collection;
import java.util.Iterator;

/**
 * The interface Top selector.
 *
 * @param <T> the type parameter
 */
public interface TopSelector<T> {


    /**
     * Select the top N elements from the iterator.
     * If more than one comparator is set, the result collection will be the union of all comparator.
     *
     * @param size the size of iterator
     * @param iterator the iterator
     * @return the collection
     */
    Collection<T> top(int size, Iterator<T> iterator);

    /**
     * Select the top N elements from the input collection.
     * If more than one comparator is set, the result collection will be the union of all comparator.
     *
     * @param inputData the input data
     * @return the top N collection
     */
    Collection<T> top(Collection<T> inputData);
}
