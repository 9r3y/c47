package com.y3r9.c47.dog.pdutest;

import java.util.ArrayList;
import java.util.List;

/**
 * The class PduBuilder.
 *
 * @version 1.0
 */
final class CopyPduBuilder implements PduBuilder {

    private Buf pdu;

    @Override
    public Buf build(final Buf buf) {
        if (pdu == null) {
            pdu = buf;
        } else {
            Buf newPdu = SingleBuf.allocate(pdu.remaining() + buf.remaining());
            while (pdu.hasRemaining()) {
                newPdu.putByte(pdu.getByte());
            }
            while (buf.hasRemaining()) {
                newPdu.putByte(buf.getByte());
            }
            newPdu.flip();
            pdu = newPdu;
        }
        return pdu;
    }

    @Override
    public void clear() {
        pdu = null;
    }

}
