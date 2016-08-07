package com.y3r9.c47.dog.pdutest;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.netty.buffer.ByteBuf;

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
        if (position < comp.offset || position >= comp.endOffset) {
            // TODO Find with two direction.
            comp = findComponent(position);
            currentComp = comp;
        }
        this.position = position;
    }

    private Component findComponent(final int position) {
        for (Component comp = headComp; comp.next != null; comp = comp.next) {
            if (position >= comp.offset && position < comp.endOffset) {
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

    private static final class Component {
        final Buf buf;

        final int offset;

        final int endOffset;

        final int length;

        Component next;

        Component(Buf buf, int offset) {
            this.buf = buf;
            this.offset = offset;
            length = buf.remaining();
            endOffset = offset + length;
        }

    }

    public void addComponent(Buf buf) {
        limit += buf.remaining();
        Component last = headComp;
        while (last.next != null) {
            last = last.next;
        }
        last.next = new Component(buf, last.endOffset);
    }

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

    private Component headComp;

    private Component currentComp;

    private int position;

    private int limit;
}
