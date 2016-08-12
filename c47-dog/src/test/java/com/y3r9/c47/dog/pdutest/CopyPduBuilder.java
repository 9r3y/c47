package com.y3r9.c47.dog.pdutest;

import java.util.ArrayList;
import java.util.List;

/**
 * The class PduBuilder.
 *
 * @version 1.0
 */
final class CopyPduBuilder implements PduBuilder {

    private SingleBuf pdu;

    @Override
    public Buf build(final Buf buf) {
        SingleBuf sBuf = (SingleBuf) buf;
        if (pdu == null) {
            pdu = sBuf;
        } else {
            SingleBuf newPdu = SingleBuf.allocate(pdu.remaining() + buf.remaining());
            newPdu.put(pdu);
            newPdu.put(sBuf);
            newPdu.flip();
            pdu = sBuf;
        }
        return pdu;
    }

    @Override
    public void clear() {
        pdu = null;
    }

}
