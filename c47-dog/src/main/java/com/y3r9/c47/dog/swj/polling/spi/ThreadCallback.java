package com.y3r9.c47.dog.swj.polling.spi;

/**
 * The Interface ThreadCallback.
 * 
 * @version 1.0
 * @since project 3.0
 */
public interface ThreadCallback {

    /**
     * Process item.
     * 
     * @return <code>true</code> if at least one item has been processed, <code>false</code> if
     *         there is no item processed
     */
    boolean processItem();

    /**
     * Notify after thread open.
     * 
     * @return true, if successful
     */
    boolean notifyAfterThreadOpen();

    /**
     * Notify before thread close.
     * 
     * @return true, if successful
     */
    boolean notifyBeforeThreadClose();
}
