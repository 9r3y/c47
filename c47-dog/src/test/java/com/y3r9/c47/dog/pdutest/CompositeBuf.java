package com.y3r9.c47.dog.pdutest;

/**
 * The class CompositeByteBuffer.
 *
 * @version 1.0
 */
final class CompositeBuf implements Buf {

    @Override
    public byte getByte() {
        Component comp = currentComp;
        while (position >= comp.endOffset) {
            comp = comp.next;
        }
        byte result = comp.buf.getByte(position - comp.offset);
        currentComp = comp;
        position++;
        return result;
    }

    @Override
    public void putByte(final byte b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getByte(final int position) {
        Component comp = currentComp;
        if (position >= comp.offset && position < comp.endOffset) {
            comp = findComponent(position);
        }
        byte result = comp.buf.getByte(position - comp.offset);
        currentComp = comp;
        this.position++;
        return result;
    }

    @Override
    public int position() {
        return position;
    }

    @Override
    public void position(final int position) {
        Component comp = currentComp;
        if (position < comp.offset || position > comp.endOffset) {
            comp = findComponent(position);
            currentComp = comp;
        }
        this.position = position;
    }

    @Override
    public int limit() {
        return limit;
    }

    @Override
    public void limit(final int position) {
        limit = position;
    }

    /**
     * Find component component.
     *
     * @param position the position
     * @return the component
     */
    private Component findComponent(final int position) {
        for (Component comp = headComp; comp != null; comp = comp.next) {
            if (position >= comp.offset && position <= comp.endOffset) {
                return comp;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public int remaining() {
        return limit - position;
    }

    @Override
    public final boolean hasRemaining() {
        return position < limit;
    }

    @Override
    public Buf duplicate() {
        CompositeBuf result = new CompositeBuf();
        result.headComp = headComp;
        result.currentComp = currentComp;
        result.position = position;
        result.limit = limit;
        return result;
    }

    @Override
    public void flip() {
        throw new UnsupportedOperationException();
    }

    /**
     * The type Component.
     *
     * @version 1.0
     */
    private static final class Component {
        /** The Buf. */
        final Buf buf;

        /** The Offset. */
        final int offset;

        /** The End offset. */
        final int endOffset;

        /** The Length. */
        final int length;

        /** The Next. */
        Component next;

        /**
         * Instantiates a new Component.
         *
         * @param buf the buf
         * @param offset the offset
         */
        Component(Buf buf, int offset) {
            this.buf = buf;
            this.offset = offset;
            length = buf.remaining();
            endOffset = offset + length;
        }

    }

    /**
     * Add component.
     *
     * @param buf the buf
     */
    public void addComponent(Buf buf) {
        limit += buf.remaining();
        if (headComp == null) {
            headComp = new Component(buf, 0);
            currentComp = headComp;
        } else {
            Component last = headComp;
            while (last.next != null) {
                last = last.next;
            }
            last.next = new Component(buf, last.endOffset);
        }
    }

    /**
     * Instantiates a new Composite buf.
     *
     * @param bufs the bufs
     */
    public CompositeBuf(final Buf ... bufs) {
        Component last = null;
        int offset = 0;
        for (Buf buf : bufs) {
            limit += buf.remaining();
            Component comp = new Component(buf, offset);
            if (last == null) {
                headComp = comp;
                currentComp = comp;
            } else {
                last.next = comp;
            }
            last = comp;
            offset += buf.remaining();
        }
    }

    /**
     * Clear.
     */
    public void clear() {
        headComp = null;
        currentComp = null;
        position = 0;
        limit = 0;
    }

    /** The Head comp. */
    private Component headComp;

    /** The Current comp. */
    private Component currentComp;

    /** The Position. */
    private int position;

    /** The Limit. */
    private int limit;
}
