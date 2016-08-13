package com.y3r9.c47.dog.swj.model.top;

import cn.com.netis.dp.commons.lang.NullArgumentException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The type Top selector provider.
 *
 * @param <T> the type parameter
 */
final class TopSelectorProvider<T> implements TopSelector<T> {

    @Override
    public Collection<T> top(final int size, final Iterator<T> iterator) {
        NullArgumentException.check(iterator, "TopSelectorProvider#iterator");
        // convert to a collection and return
        if (size <= maxSize) {
            final List<T> result = new ArrayList<>(size);
            iterator.forEachRemaining(result::add);
            return result;
        }

        iterator.forEachRemaining(
                element -> minHeaps.forEach(heap -> heap.insertWithOverflow(element)));

        // union all top element arrays
        final Set<T> unionSet = new HashSet<>();
        minHeaps.forEach(heaps -> {
            Collections.addAll(unionSet, heaps.getHeapArray());
            heaps.clear(); // clear heap in the end
        });
        return unionSet;
    }

    @Override
    public Collection<T> top(final Collection<T> inputData) {
        NullArgumentException.check(inputData, "TopSelectorProvider#inputData");
        // if collection size is less than or equal max size, return original collection directly.
        if (inputData.size() <= maxSize) {
            return inputData;
        }
        // build minimal heap
        inputData.stream()
                .forEach(element -> minHeaps.forEach(heap -> heap.insertWithOverflow(element)));
        // union all top element arrays
        final Set<T> unionSet = new HashSet<>();
        minHeaps.forEach(heap -> {
            Collections.addAll(unionSet, heap.getHeapArray());
            heap.clear(); // clear heap in the end
        });
        return unionSet;
    }

    /**
     * Instantiates a new Top selector provider.
     *
     * @param size the size
     * @param comparators the comparators
     */
    TopSelectorProvider(final int size, final Collection<Comparator<T>> comparators) {
        maxSize = size;
        comparators.forEach(comparator -> minHeaps.add(new MinHeap<>(size, comparator)));
    }

    /** The Queues. */
    private final List<MinHeap<T>> minHeaps;

    /** The Max size. */
    private final int maxSize;

    {
        minHeaps = new LinkedList<>();
    }

}
