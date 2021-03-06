package com.y3r9.c47.dog.swj.model.collection;

import org.apache.commons.lang3.tuple.Pair;

import com.y3r9.c47.dog.swj.model.collection.spi.PairDualQueue;

/**
 * The Class SafeInPairDualQueue, which contains dual queues: the IN queue and the OUT queue. The
 * OUT queue is <em>thread-safe</em> for access, but the IN queue is not.
 * 
 * @param <H> the generic type
 * @param <D> the generic type
 * 
 * @version 1.5
 * @see PairDualQueue
 * @since project 1.0
 */
public final class SafeOutPairDualQueue<H, D> extends SafeOutDualQueue<Pair<H, D>> implements
        PairDualQueue<H, D> {

    @Override
    public void addToInQueue(final H header, final D data) {
        addToInQueue(Pair.of(header, data));
    }

    @Override
    public H peekOutQueueForHeader() {
        return peekFromOutQueue().getLeft();
    }

    @Override
    public D removeOutQueueForData() {
        return removeFromOutQueue().getRight();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SafeOutPairDualQueue [toStrig()=").append(super.toString()).append("]");
        return builder.toString();
    }

}
