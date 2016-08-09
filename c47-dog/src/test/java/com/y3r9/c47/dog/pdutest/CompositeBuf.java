package com.y3r9.c47.dog.pdutest;

import java.util.ArrayList;
import java.util.List;

/**
 * The class CompositeByteBuffer.
 *
 * @version 1.0
 */
final class CompositeBuf implements Buf {

    @Override
    public byte getByte() {
        int index = currentIndex;
        Component comp;
        for (;;) {
            comp = components.get(index);
            if (position >= comp.endOffset) {
                index++;
            } else {
                break;
            }
        }
        currentIndex = index;
        byte result = comp.buf.getByte(position - comp.offset);
        position++;
        return result;
    }

    @Override
    public void putByte(final byte b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getByte(final int position) {
        Component comp = components.get(currentIndex);
        if (position >= comp.offset && position < comp.endOffset) {
            comp = findComponent(position);
        }
        return comp.buf.getByte(position - comp.offset);
    }

    @Override
    public int position() {
        return position;
    }

    @Override
    public void position(final int position) {
        this.position = position;
    }

    private Component findComponent(final int position) {
        for (int low = 0, high = components.size(); low <= high;) {
            int mid = low + high >>> 1;
            Component c = components.get(mid);
            if (position >= c.endOffset) {
                low = mid + 1;
            } else if (position < c.offset) {
                high = mid - 1;
            } else {
                assert c.length != 0;
                return c;
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
        result.components = components;
        result.currentIndex = currentIndex;
        result.position = position;
        result.limit = limit;
        return result;
    }

    @Override
    public void flip() {
        throw new UnsupportedOperationException();
    }

    private static final class Component {
        final Buf buf;

        final int offset;

        final int endOffset;

        final int length;

        Component(Buf buf, int offset) {
            this.buf = buf;
            this.offset = offset;
            length = buf.remaining();
            endOffset = offset + length;
        }

    }

    public void addComponent(Buf buf) {
        limit += buf.remaining();
        Component last = null;
        for (Component comp : components) {
            last = comp;
        }
        if (last == null) {
            Component comp = new Component(buf, 0);
            components.add(comp);
            currentIndex = 0;
        } else {
            Component comp = new Component(buf, last.endOffset);
            components.add(comp);
        }
    }

    public CompositeBuf(final Buf ... bufs) {
        int offset = 0;
        for (Buf buf : bufs) {
            limit += buf.remaining();
            Component comp = new Component(buf, offset);
            components.add(comp);
            offset += buf.remaining();
        }
        if (components.size() == 1) {
            currentIndex = 0;
        }
    }

    public void clear() {
        components.clear();
        currentIndex = 0;
        position = 0;
        limit = 0;
    }

    private List<Component> components = new ArrayList<>();

    private int currentIndex;

    private int position;

    private int limit;
}
