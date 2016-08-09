package com.y3r9.c47.dog.pdutest;

/**
 * The interface PduBuilder.
 *
 * @version 1.0
 */
interface PduBuilder {

    Buf build(Buf buf);

    void clear();
}
