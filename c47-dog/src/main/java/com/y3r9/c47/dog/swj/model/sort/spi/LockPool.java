package com.y3r9.c47.dog.swj.model.sort.spi;

/**
 * The Interface LockPool.
 * 
 * @version 1.0
 * @since project 3.0
 */
public interface LockPool {

    /** The minimal size. */
    int MIN_SIZE = 16;

    /** The maximal size. */
    int MAX_SIZE = 256 * 1024;

    /** The default size. */
    int DEFAULT_SIZE = 64;

    /**
     * Sets the pool size.
     * 
     * @param value the new pool size
     */
    void setPoolSize(int value);

    /**
     * Gets the pool size.
     * 
     * @return the pool size
     */
    int getPoolSize();

    /**
     * Lock.
     * 
     * @param index the index
     */
    void lock(int index);

    /**
     * Try lock.
     * 
     * @param index the index
     * @return true, if successful
     */
    boolean tryLock(int index);

    /**
     * Unlock.
     * 
     * @param index the index
     */
    void unlock(int index);
}
