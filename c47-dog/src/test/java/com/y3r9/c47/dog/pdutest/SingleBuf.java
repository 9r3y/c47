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
        byte result = buf.get(position);
        position++;
        return result;
    }

    @Override
    public void putByte(byte b) {
        buf.put(b);
        position++;
    }

    @Override
    public byte getByte(final int position) {
        return buf.get(position);
    }

    @Override
    public int position() {
        return position;
    }

    @Override
    public void position(final int position) {
        this.position = position;
    }

    @Override
    public int remaining() {
        return limit - position;
    }

    @Override
    public boolean hasRemaining() {
        return position < limit;
    }

    @Override
    public Buf duplicate() {
        return new SingleBuf(buf);
    }

    @Override
    public void flip() {
        buf.flip();
        limit = position;
        position = 0;
    }

    public SingleBuf(final ByteBuffer buf) {
        this.buf = buf;
        this.limit = buf.remaining();
    }

    public static Buf allocate(final int size) {
        ByteBuffer bb = ByteBuffer.allocate(size);
        return new SingleBuf(bb);
    }

    private final ByteBuffer buf;

    private int position;

    private int limit;
}
