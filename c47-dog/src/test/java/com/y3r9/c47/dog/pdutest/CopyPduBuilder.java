package com.y3r9.c47.dog.pdutest;

/**
 * The class PduBuilder.
 *
 * @version 1.0
 */
final class CopyPduBuilder implements PduBuilder {

    /** The Pdu. */
    private ExpandBuf pdu = new ExpandBuf();

    @Override
    public Buf build(final Buf buf) {
        SingleBuf sBuf = (SingleBuf) buf;
        pdu.addBuf(sBuf);
        return pdu.get();
    }

    @Override
    public void clear() {
//        pdu.clear();
        pdu = new ExpandBuf();
    }

}
