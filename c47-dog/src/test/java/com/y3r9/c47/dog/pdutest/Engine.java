package com.y3r9.c47.dog.pdutest;

import java.nio.ByteBuffer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The class Engine.
 *
 * @version 1.0
 */
public final class Engine {

    static long PDU_COUNT = 1000000;

    @Test
    public void run() {
        ByteBuffer bb = ByteBuffer.allocate(Decoder.MTU);
        Buf pktBuf = new SingleBuf(bb);

        CompositePduBuilder pduBuilder = new CompositePduBuilder();
//        PduBuilder pduBuilder = new CopyPduBuilder();
        Decoder decoder = new Decoder();
        doRun(pktBuf, pduBuilder, decoder);

        long start = System.currentTimeMillis();
        doRun(pktBuf, pduBuilder, decoder);
        System.out.println(System.currentTimeMillis() - start);
    }

    public void doRun(Buf pktBuf, PduBuilder pduBuilder, Decoder decoder) {
        long pktCount = PDU_COUNT * Decoder.PDU_PKT_COUNT;
        for (long i = 0; i < pktCount; i++) {
            pktBuf.position(0);
            Buf buf = pduBuilder.build(pktBuf);
            if (decoder.decode(buf.duplicate())) {
                pduBuilder.clear();
            }
        }
    }

    @Test
    public void testCompositeBuf() {
        ByteBuffer bb1 = ByteBuffer.allocate(10);
        SingleBuf buf1 = new SingleBuf(bb1);
        ByteBuffer bb2 = ByteBuffer.allocate(10);
        SingleBuf buf2 = new SingleBuf(bb2);
        CompositeBuf buf = new CompositeBuf(buf1, buf2);

        assertEquals(20, buf.remaining());
        assertEquals(0, buf.position());
        assertTrue(buf.hasRemaining());

        buf.getByte();
        assertEquals(19, buf.remaining());
        assertEquals(1, buf.position());
        assertTrue(buf.hasRemaining());

        buf.position(10);
        assertEquals(10, buf.remaining());
        assertEquals(10, buf.position());
        assertTrue(buf.hasRemaining());

        buf.position(0);
        assertEquals(20, buf.remaining());
        assertEquals(0, buf.position());
        assertTrue(buf.hasRemaining());

        buf.position(20);
        assertEquals(0, buf.remaining());
        assertEquals(20, buf.position());
        assertFalse(buf.hasRemaining());

        buf.position(0);
        for (int i = 0; i < 20; i++) {
            buf.getByte();
        }
        buf.position(0);
        for (int i = 0; i < 20; i++) {
            buf.getByte();
        }
        buf.position(10);
        for (int i = 0; i < 10; i++) {
            buf.getByte();
        }
        buf.position(11);
        for (int i = 0; i < 9; i++) {
            buf.getByte();
        }
    }

    @Test
    public void testSingleBuf() {
        ByteBuffer bb = ByteBuffer.allocate(10);
        SingleBuf buf = new SingleBuf(bb);

        assertEquals(10, buf.remaining());
        assertEquals(0, buf.position());
        assertTrue(buf.hasRemaining());

        buf.getByte();
        assertEquals(9, buf.remaining());
        assertEquals(1, buf.position());
        assertTrue(buf.hasRemaining());

        buf.position(5);
        assertEquals(5, buf.remaining());
        assertEquals(5, buf.position());
        assertTrue(buf.hasRemaining());

        buf.position(0);
        assertEquals(10, buf.remaining());
        assertEquals(0, buf.position());
        assertTrue(buf.hasRemaining());

        buf.position(10);
        assertEquals(0, buf.remaining());
        assertEquals(10, buf.position());
        assertFalse(buf.hasRemaining());
    }

}
