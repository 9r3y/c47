package com.y3r9.c47.dog.swj.model.sort;

import java.util.concurrent.locks.ReentrantLock;

import com.y3r9.c47.dog.swj.value.Power2Utils;
import com.y3r9.c47.dog.swj.model.sort.spi.LockPool;

/**
 * The Class LockPoolProvider.
 * 
 * @version 1.0
 * @see LockPool
 * @since project 3.0
 */
public class LockPoolProvider implements LockPool {

    @Override
    public final void setPoolSize(final int value) {
        final int size = Power2Utils.power2Ceil(value, MIN_SIZE, MAX_SIZE);
        indexMask = size - 1;

        pool = new ReentrantLock[size];
        for (int i = 0; i < size; i++) {
            pool[i] = new ReentrantLock();
        }
    }

    @Override
    public final int getPoolSize() {
        return indexMask + 1;
    }

    @Override
    public final void lock(final int index) {
        pool[getPoolIndex(index)].lock();
    }

    @Override
    public final boolean tryLock(final int index) {
        return pool[getPoolIndex(index)].tryLock();
    }

    @Override
    public final void unlock(final int index) {
        pool[getPoolIndex(index)].unlock();
    }

    /**
     * Gets the index.
     * 
     * @param index the index
     * @return the index
     */
    protected final int getPoolIndex(final int index) {
        return index & indexMask;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("LockPoolProvider [indexMask=").append(indexMask).append(", getPoolSize()=")
                .append(getPoolSize()).append(']');
        return builder.toString();
    }

    /**
     * Instantiates a new lock pool provider.
     * 
     * @param size the size
     */
    public LockPoolProvider(final int size) {
        setPoolSize(size);
    }

    /**
     * Instantiates a new lock pool provider.
     */
    public LockPoolProvider() {
        this(DEFAULT_SIZE);
    }

    /** The index mask. */
    private transient int indexMask;

    /** The pool. */
    private transient ReentrantLock[] pool;
}
