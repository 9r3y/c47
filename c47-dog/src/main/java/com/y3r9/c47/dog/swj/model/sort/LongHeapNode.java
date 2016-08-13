package com.y3r9.c47.dog.swj.model.sort;

import com.y3r9.c47.dog.swj.model.sort.spi.LongHeapIn;
import com.y3r9.c47.dog.swj.model.sort.spi.LongHeapOut;

/**
 * The Class LongHeapNode.
 * 
 * @param <V> the value type
 * @version 1.0
 * @see LongHeapIn, LongHeapOut
 * @since project 3.0
 */
public class LongHeapNode<V> implements LongHeapIn<V>, LongHeapOut<V> {

    @Override
    public final boolean isReady() {
        return ready;
    }

    @Override
    public final long peekKey() {
        return selectLeft ? left.peekKey() : right.peekKey();
    }

    @Override
    public final V peekValue() {
        return selectLeft ? left.peekValue() : right.peekValue();
    }

    @Override
    public final void pop() {
        if (ready) {
            if (selectLeft) {
                left.pop();
            } else {
                right.pop();
            }
            updateState();
        }
    }

    @Override
    public final boolean setLeft(final LongHeapOut<V> queue) {
        if (queue == null) {
            return false;
        }
        left = queue;
        updateState();
        return true;
    }

    @Override
    public final LongHeapOut<V> getLeft() {
        return left;
    }

    @Override
    public final boolean setRight(final LongHeapOut<V> queue) {
        if (queue == null) {
            return false;
        }
        right = queue;
        updateState();
        return true;
    }

    @Override
    public final LongHeapOut<V> getRight() {
        return right;
    }

    /**
     * Checks if is select left.
     * 
     * @return true, if is select left
     */
    public final boolean isSelectLeft() {
        return selectLeft;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("LongHeapNode [ready=").append(ready).append(", selectLeft=")
                .append(selectLeft).append(", left=").append(left).append(", right=").append(right)
                .append(']');
        return builder.toString();
    }

    /**
     * Update state.
     */
    private void updateState() {
        ready = left.isReady() && right.isReady();
        selectLeft = left.peekKey() < right.peekKey();
    }

    /** The ready. */
    private transient boolean ready;

    /** The select left. */
    private transient boolean selectLeft;

    /** The left. */
    private LongHeapOut<V> left;

    /** The right. */
    private LongHeapOut<V> right;

    {
        left = new LongHeapDummyOut<V>();
        right = new LongHeapDummyOut<V>();
    }
}
