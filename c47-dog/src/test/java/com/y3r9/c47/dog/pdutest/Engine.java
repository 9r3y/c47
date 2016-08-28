package com.y3r9.c47.dog.pdutest;

import java.nio.ByteBuffer;

/**
 * The class Engine.
 *
 * @version 1.0
 */
public final class Engine {

    /** The Mtu. */
    static int MTU = 1500;

    /**
     * Run.
     */
    public void run(final PduBuilder pduBuilder, final long pktCount, final int pduLen) {
        final ByteBuffer bb = ByteBuffer.allocate(MTU);
        final Decoder decoder = new Decoder(pduLen);

        for (long i = 0; i < pktCount; i++) {
            final Buf pktBuf = new SingleBuf(bb.duplicate());
            Buf buf = pduBuilder.build(pktBuf);
            if (decoder.decode(buf.duplicate())) {
                pduBuilder.clear();
            }
        }
    }

}
