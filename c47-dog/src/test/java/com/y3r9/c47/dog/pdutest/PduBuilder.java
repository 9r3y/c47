package com.y3r9.c47.dog.pdutest;

/**
 * The interface PduBuilder.
 *
 * @version 1.0
 */
interface PduBuilder {

    /**
     * Build buf.
     *
     * @param buf the buf
     * @return the buf
     */
    Buf build(Buf buf);

    /**
     * Clear.
     */
    void clear();
}
