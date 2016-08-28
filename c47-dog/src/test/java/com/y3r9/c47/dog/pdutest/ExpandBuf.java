package com.y3r9.c47.dog.pdutest;

/**
 * The class ExpandBuf.
 *
 * @version 1.0
 */
final class ExpandBuf implements Buf {
    @Override
    public byte getByte() {
        return buf.getByte();
    }

    @Override
    public void putByte(final byte b) {
        buf.putByte(b);
    }

    @Override
    public byte getByte(final int position) {
        return buf.getByte(position);
    }

    @Override
    public int position() {
        return buf.position();
    }

    @Override
    public void position(final int position) {
        buf.position(position);
    }

    @Override
    public int limit() {
        return buf.limit();
    }

    @Override
    public void limit(final int position) {
        buf.limit(position);
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
    public ExpandBuf duplicate() {
        return new ExpandBuf(buf.duplicate());
    }

    @Override
    public void flip() {
        buf.flip();
    }

    /**
     * Get buf.
     *
     * @return the buf
     */
    public Buf get() {
        Buf result = buf.duplicate();
        result.flip();
        return result;
    }

    /**
     * Add buf.
     *
     * @param buf the buf
     */
    public void addBuf(final SingleBuf buf) {
        ensureCapacition(buf.remaining());
        this.buf.put(buf);
    }

    /**
     * Ensure capacition.
     *
     * @param len the len
     */
    private void ensureCapacition(final int len) {
        final int required = buf.position() + len;
        final int current = buf.limit();
        int newLen = current;
        while (newLen < required) {
            newLen = newLen << 1;
        }
        if (newLen != current) {
            final SingleBuf newBuf = SingleBuf.allocate(newLen);
            buf.flip();
            newBuf.put(buf);
            buf = newBuf;
        }
    }

    /**
     * Clear.
     */
    public void clear() {
        buf.position(0);
    }

    /**
     * Instantiates a new Expand buf.
     *
     * @param buf the buf
     */
    private ExpandBuf(final SingleBuf buf) {
        this.buf = buf;
    }

    /**
     * Instantiates a new Expand buf.
     */
    public ExpandBuf() {
        this.buf = SingleBuf.allocate(1024);
    }

    /** The Buf. */
    private SingleBuf buf;
}
