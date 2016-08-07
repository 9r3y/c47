package com.y3r9.c47.dog.pdutest;

/**
 * The interface Buf.
 *
 * @version 1.0
 */
interface Buf {

    byte getByte();

    byte getByte(int position);

    int position();

    void position(int position);

    int remaining();

    boolean hasRemaining();
}
