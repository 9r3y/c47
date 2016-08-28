package com.y3r9.c47.dog.pdutest;

/**
 * The interface Buf.
 *
 * @version 1.0
 */
interface Buf {

    /**
     * Gets byte.
     *
     * @return the byte
     */
    byte getByte();

    /**
     * Put byte.
     *
     * @param b the b
     */
    void putByte(byte b);

    /**
     * Gets byte.
     *
     * @param position the position
     * @return the byte
     */
    byte getByte(int position);

    /**
     * Position int.
     *
     * @return the int
     */
    int position();

    /**
     * Position.
     *
     * @param position the position
     */
    void position(int position);

    /**
     * Limit int.
     *
     * @return the int
     */
    int limit();

    /**
     * Limit.
     *
     * @param position the position
     */
    void limit(int position);

    /**
     * Remaining int.
     *
     * @return the int
     */
    int remaining();

    /**
     * Has remaining boolean.
     *
     * @return the boolean
     */
    boolean hasRemaining();

    /**
     * Duplicate buf.
     *
     * @return the buf
     */
    Buf duplicate();

    /**
     * Flip.
     */
    void flip();
}
