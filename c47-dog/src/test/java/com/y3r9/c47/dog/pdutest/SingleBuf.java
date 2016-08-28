package com.y3r9.c47.dog.pdutest;

import java.nio.ByteBuffer;

/**
 * The class SingleBuf.
 *
 * @version 1.0
 */
final class SingleBuf implements Buf {

    @Override
    public byte getByte() {
        return buf.get();
    }

    @Override
    public void putByte(byte b) {
        buf.put(b);
    }

    /**
     * Put.
     *
     * @param src the src
     */
    public void put(final SingleBuf src) {
        buf.put(src.buf);
    }

    @Override
    public byte getByte(final int position) {
        return buf.get(position - offset);
    }

    @Override
    public int position() {
        return buf.position() - offset;
    }

    @Override
    public void position(final int position) {
        buf.position(offset + position);
    }

    @Override
    public int limit() {
        return buf.limit() - offset;
    }

    @Override
    public void limit(final int position) {
        buf.limit(offset + position);
    }

    @Override
    public int remaining() {
        return buf.remaining();
    }

    @Override
    public boolean hasRemaining() {
        return buf.hasRemaining();
    }

    @Override
    public SingleBuf duplicate() {
        final ByteBuffer newBuf = buf.duplicate();
        newBuf.position(offset);
        final SingleBuf result = new SingleBuf(newBuf);
        newBuf.position(buf.position());
        return result;
    }

    @Override
    public void flip() {
        buf.limit(buf.position());
        buf.position(offset);
    }

    /**
     * Instantiates a new Single buf.
     *
     * @param buf the buf
     */
    public SingleBuf(final ByteBuffer buf) {
        this.buf = buf;
        offset = buf.position();
    }

    /**
     * Allocate single buf.
     *
     * @param size the size
     * @return the single buf
     */
    public static SingleBuf allocate(final int size) {
        ByteBuffer bb = ByteBuffer.allocate(size);
        return new SingleBuf(bb);
    }

    /** The Buf. */
    private final ByteBuffer buf;

    private final int offset;

}
