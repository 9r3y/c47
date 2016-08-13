package com.y3r9.c47.dog.swj.polling;

import com.y3r9.c47.dog.swj.polling.spi.ThreadCallback;

/**
 * The Class AbstractIdleSleepThread.
 * 
 * <pre>
 * XML example for configuration:
 * 
 * <...>
 *   <idleRetryCount>8</idleRetryCount>
 *   <ideaSleepNano>1</ideaSleepNano>
 * </...>
 * 
 * </pre>
 * 
 * @version 1.0
 * @see Thread, Threadable, ThreadCallback
 * @since project 3.0
 */
public abstract class AbstractIdleSleepThread extends CallbackIdleSleepThread implements
        ThreadCallback {

    /**
     * Instantiates a new abstract thread node.
     */
    public AbstractIdleSleepThread() {
        super();
    }

    /**
     * Instantiates a new abstract thread node.
     * 
     * @param name the name
     */
    public AbstractIdleSleepThread(final String name) {
        super(name);
    }

    /**
     * Instantiates a new abstract thread node.
     * 
     * @param id the id
     * @param name the name
     */
    public AbstractIdleSleepThread(final int id, final String name) {
        super(id, name);
        // bind thread callback
        finalSetCallback(this);
    }
}
