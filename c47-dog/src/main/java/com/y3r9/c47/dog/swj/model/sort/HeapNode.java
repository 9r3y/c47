package com.y3r9.c47.dog.swj.model.sort;

import com.y3r9.c47.dog.swj.model.sort.spi.HeapIn;
import com.y3r9.c47.dog.swj.model.sort.spi.HeapOut;

/**
 * The Class HeapNode.
 * 
 * @param <V> the value type
 * @version 1.0
 * @see HeapIn, HeapOut
 * @since project 3.0
 */
public class HeapNode<V> implements HeapIn<V>, HeapOut<V> {

    @Override
    public final boolean hasNext() {
        if (hasNext) {
            hasNext = left.hasNext() || right.hasNext();
        }
        return hasNext;
    }

    @Override
    public final boolean isNextReady() {
        if (!ready) {
            updateReadyState();
        }
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
    public final V pop() {
        final V value = selectLeft ? left.pop() : right.pop();
        updateReadyState();
        return value;
    }

    @Override
    public final boolean setLeft(final HeapOut<V> queue) {
        if (queue == null) {
            return false;
        }
        left = queue;
        updateState();
        return true;
    }

    @Override
    public final HeapOut<V> getLeft() {
        return left;
    }

    @Override
    public final boolean setRight(final HeapOut<V> queue) {
        if (queue == null) {
            return false;
        }
        right = queue;
        updateState();
        return true;
    }

    @Override
    public final HeapOut<V> getRight() {
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
        builder.append("HeapNode [ready=").append(ready).append(", selectLeft=")
                .append(selectLeft).append(", left=").append(left).append(", right=").append(right)
                .append(']');
        return builder.toString();
    }

    /**
     * Update ready state.
     */
    private void updateReadyState() {
        if (!left.hasNext()) {
            // Left is not has next;
            ready = right.isNextReady();
            selectLeft = !ready;
            if (ready) {
                selectLeft = false;
            }
        } else if (!right.hasNext()) {
            // Left is has next, right is not has next;
            ready = left.isNextReady();
            selectLeft = ready;
        } else {
            // Left and right is all has next;
            ready = left.isNextReady() && right.isNextReady();
            if (ready) {
                final long leftKey = left.peekKey();
                selectLeft = leftKey < right.peekKey();
            } else {
                selectLeft = false;
            }
        }
    }

    /**
     * Update state.
     */
    private void updateState() {
        if (hasNext()) {
            updateReadyState();
        }
    }

    /** The left ready. */
    private transient boolean ready;

    /** The has next. */
    private transient boolean hasNext;

    /** The select left. */
    private transient boolean selectLeft;

    /** The left. */
    private HeapOut<V> left;

    /** The right. */
    private HeapOut<V> right;

    {
        left = new HeapDummyOut<V>();
        right = new HeapDummyOut<V>();
        hasNext = true;
    }
}
